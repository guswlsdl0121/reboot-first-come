package com.hyunjin.session.repository;

import com.hyunjin.session.constants.RedisFields;
import com.hyunjin.session.constants.RedisKeys;
import com.hyunjin.session.exception.SessionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.MapSession;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Slf4j
@Component
public class RedisIndexSessionRepository implements CustomSessionRepository {
    private static final int MAX_SESSIONS_PER_USER = 3;

    private final RedisTemplate<String, Object> redisTemplate;
    private final Duration defaultMaxInactiveInterval;

    public RedisIndexSessionRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.defaultMaxInactiveInterval = Duration.ofMinutes(30);
    }

    @Override
    public MapSession createSession() {
        try {
            MapSession session = new MapSession();
            session.setMaxInactiveInterval(defaultMaxInactiveInterval);
            return session;
        } catch (Exception e) {
            throw new SessionException("세션 생성에 실패했습니다", e);
        }
    }

    @Override
    public void save(MapSession session) {
        try {
            // Step 1: 세션 ID와 사용자 ID 추출
            String sessionId = session.getId();
            Object memberId = session.getAttribute(PRINCIPAL_NAME_INDEX_NAME);
            Object securityContext = session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);

            if (Objects.isNull(memberId)) {
                return;
            }

            // Step 2: 현재 사용자의 세션 목록 조회
            String principalKey = RedisKeys.getIndexKey(memberId.toString());
            Set<Object> existingSessions = redisTemplate.opsForSet().members(principalKey);

            // Step 3: 세션 수 제한 처리
            if (existingSessions != null && existingSessions.size() >= MAX_SESSIONS_PER_USER) {
                // 가장 오래된 세션을 찾아서 삭제
                List<SessionInfo> sessionInfoList = existingSessions.stream()
                        .map(String.class::cast)
                        .map(sid -> {
                            String key = RedisKeys.getSessionKey(sid);
                            Long lastAccessed = (Long) redisTemplate.opsForHash().get(key, RedisFields.LAST_ACCESSED_TIME);
                            return new SessionInfo(sid, lastAccessed != null ? lastAccessed : 0L);
                        })
                        .sorted(Comparator.comparingLong(SessionInfo::lastAccessTime))
                        .toList();

                // 최대 세션 수를 초과하는 만큼 오래된 세션 삭제
                int sessionsToRemove = sessionInfoList.size() - MAX_SESSIONS_PER_USER + 1;
                sessionInfoList.stream()
                        .limit(sessionsToRemove)
                        .forEach(info -> deleteById(info.sessionId()));
            }

            // Step 4: 새로운 세션 데이터 구성
            Map<String, Object> sessionData = new HashMap<>();
            sessionData.put(RedisFields.SECURITY_CONTEXT, securityContext);
            sessionData.put(RedisFields.PRINCIPAL_NAME, memberId);
            sessionData.put(RedisFields.LAST_ACCESSED_TIME, System.currentTimeMillis());

            // Step 5: Redis에 세션 데이터 저장
            String sessionKey = RedisKeys.getSessionKey(sessionId);
            redisTemplate.opsForHash().putAll(sessionKey, sessionData);
            redisTemplate.expire(sessionKey, session.getMaxInactiveInterval());

            // Step 6: 사용자 ID 인덱스에 세션 ID 추가
            redisTemplate.opsForSet().add(principalKey, sessionId);
            redisTemplate.expire(principalKey, session.getMaxInactiveInterval());

        } catch (Exception e) {
            throw new SessionException("세션 저장에 실패했습니다", e);
        }
    }

    @Override
    public MapSession findById(String id) {
        try {
            // Step 1: Redis에서 세션 데이터 조회
            String sessionKey = RedisKeys.getSessionKey(id);
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(sessionKey);

            if (entries.isEmpty()) {
                return null;
            }

            // Step 2: 세션 객체 생성 및 기본 설정
            MapSession session = new MapSession(id);
            session.setMaxInactiveInterval(defaultMaxInactiveInterval);

            // Step 3: 조회된 세션 데이터를 세션 객체에 복원
            entries.forEach((key, value) -> {
                if (key instanceof String) {
                    session.setAttribute((String) key, value);
                }
            });

            return session;

        } catch (Exception e) {
            throw new SessionException("세션 조회에 실패했습니다", e);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            // Step 1: 세션키로 데이터 조회
            String sessionKey = RedisKeys.getSessionKey(id);
            Map<Object, Object> sessionData = redisTemplate.opsForHash().entries(sessionKey);

            // Step 2: 인덱스에서 세션 ID 제거
            if (!sessionData.isEmpty()) {
                Object memberId = sessionData.get(RedisFields.PRINCIPAL_NAME);
                if (Objects.nonNull(memberId)) {
                    String principalKey = RedisKeys.getIndexKey(memberId.toString());
                    redisTemplate.opsForSet().remove(principalKey, id);
                }
            }

            // Step 3: 세션 데이터 삭제
            redisTemplate.delete(sessionKey);
        } catch (Exception e) {
            throw new SessionException("세션 삭제에 실패했습니다", e);
        }
    }

    @Override
    public Map<String, MapSession> findByIndexNameAndIndexValue(String indexName, String indexValue) {
        // Step 1: 인덱스 이름 검증
        if (!PRINCIPAL_NAME_INDEX_NAME.equals(indexName)) {
            return Collections.emptyMap();
        }

        try {
            // Step 2: 사용자 ID 인덱스로 세션 ID 목록 조회
            String principalKey = RedisKeys.getIndexKey(indexValue);
            Set<Object> sessionIds = redisTemplate.opsForSet().members(principalKey);

            if (CollectionUtils.isEmpty(sessionIds)) {
                return Collections.emptyMap();
            }

            // Step 3: 세션 ID 목록으로 세션 데이터 조회 및 변환
            return sessionIds.stream()
                    .map(String.class::cast)
                    .map(this::findById)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(MapSession::getId, session -> session));

        } catch (Exception e) {
            throw new SessionException("세션 인덱스 조회에 실패했습니다", e);
        }
    }

    @Override
    public void deleteAllByIndex(String memberId) {
        try {
            // Step 1: 사용자 ID로 모든 세션 ID 조회
            String principalKey = RedisKeys.getIndexKey(memberId);
            Set<Object> sessionIds = redisTemplate.opsForSet().members(principalKey);

            if (CollectionUtils.isEmpty(sessionIds)) {
                return;
            }

            // Step 2: 조회된 모든 세션 삭제
            sessionIds.stream()
                    .map(String.class::cast)
                    .forEach(this::deleteById);

            // Step 3: 사용자 ID 인덱스 삭제
            redisTemplate.delete(principalKey);
        } catch (Exception e) {
            throw new SessionException("사용자의 모든 세션 삭제에 실패했습니다", e);
        }
    }

    private record SessionInfo(String sessionId, Long lastAccessTime) {
    }
}