package com.hyunjin.session.repository;

import com.hyunjin.session.constants.RedisField;
import com.hyunjin.session.constants.RedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.MapSession;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import com.hyunjin.session.exception.SessionException;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
public class RedisIndexSessionRepository implements CustomSessionRepository {
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

            if (Objects.isNull(memberId)) {
                return;
            }

            // Step 2: 기존 사용자 세션 모두 삭제
            findByIndexNameAndIndexValue(PRINCIPAL_NAME_INDEX_NAME, memberId.toString())
                    .keySet()
                    .forEach(this::deleteById);

            // Step 3: 새로운 세션 데이터 구성
            Map<String, Object> sessionData = new HashMap<>();
            Object securityContext = session.getAttribute(RedisField.SECURITY_CONTEXT);
            sessionData.put(RedisField.SECURITY_CONTEXT, securityContext);
            sessionData.put(RedisField.PRINCIPAL_NAME, memberId);
            sessionData.put(RedisField.LAST_ACCESSED_TIME, System.currentTimeMillis());

            // Step 4: Redis에 세션 데이터 저장 및 만료시간 설정
            redisTemplate.opsForHash().putAll(RedisKey.SESSION + sessionId, sessionData);
            redisTemplate.expire(RedisKey.SESSION + sessionId, session.getMaxInactiveInterval());

            // Step 5: 사용자 ID 인덱스에 세션 ID 추가 및 만료시간 설정
            redisTemplate.opsForSet().add(RedisKey.PRINCIPAL_NAME_INDEX + memberId, sessionId);
            redisTemplate.expire(RedisKey.PRINCIPAL_NAME_INDEX + memberId, session.getMaxInactiveInterval());

        } catch (Exception e) {
            throw new SessionException("세션 저장에 실패했습니다", e);
        }
    }

    @Override
    public MapSession findById(String id) {
        try {
            // Step 1: Redis에서 세션 데이터 조회
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(RedisKey.SESSION + id);

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
            // Step 1: 세션 데이터 조회
            Map<Object, Object> sessionData = redisTemplate.opsForHash().entries(RedisKey.SESSION + id);

            if (!sessionData.isEmpty()) {
                // Step 2: 사용자 ID 인덱스에서 세션 ID 제거
                Object memberId = sessionData.get(RedisField.PRINCIPAL_NAME);
                if (Objects.nonNull(memberId)) {
                    redisTemplate.opsForSet().remove(RedisKey.PRINCIPAL_NAME_INDEX + memberId, id);
                }

                // Step 3: 세션 데이터 삭제
                redisTemplate.delete(RedisKey.SESSION + id);
            }
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
            Set<Object> sessionIds = redisTemplate.opsForSet().members(RedisKey.PRINCIPAL_NAME_INDEX + indexValue);

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
            Set<Object> sessionIds = redisTemplate.opsForSet().members(RedisKey.PRINCIPAL_NAME_INDEX + memberId);

            if (CollectionUtils.isEmpty(sessionIds)) {
                return;
            }

            // Step 2: 조회된 모든 세션 삭제
            sessionIds.stream()
                    .map(String.class::cast)
                    .forEach(this::deleteById);

            // Step 3: 사용자 ID 인덱스 삭제
            redisTemplate.delete(RedisKey.PRINCIPAL_NAME_INDEX + memberId);
        } catch (Exception e) {
            throw new SessionException("사용자의 모든 세션 삭제에 실패했습니다", e);
        }
    }
}