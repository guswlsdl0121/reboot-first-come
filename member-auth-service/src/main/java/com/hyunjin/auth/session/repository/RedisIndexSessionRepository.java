package com.hyunjin.auth.session.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.MapSession;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Primary
@Component
public class RedisIndexSessionRepository implements CustomSessionRepository {
    private static final String BASE_PREFIX = "spring:session:";
    private static final String SESSIONS_PREFIX = BASE_PREFIX + "sessions:";
    private static final String SESSION_KEY_PREFIX = SESSIONS_PREFIX + "session:";
    private static final String EXPIRES_KEY_PREFIX = SESSIONS_PREFIX + "expires:";
    private static final String INDEX_PREFIX = BASE_PREFIX + "index:";
    private static final String PRINCIPAL_NAME_INDEX_PREFIX = INDEX_PREFIX + FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME + ":";

    private final RedisTemplate<String, Object> redisTemplate;
    private final Duration defaultMaxInactiveInterval;

    public RedisIndexSessionRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.defaultMaxInactiveInterval = Duration.ofMinutes(30);
    }

    @Override
    public MapSession createSession() {
        MapSession session = new MapSession();
        session.setMaxInactiveInterval(defaultMaxInactiveInterval);
        return session;
    }

    @Override
    public void save(MapSession session) {
        String sessionId = session.getId();
        deleteById(sessionId);

        String sessionKey = getSessionKey(sessionId);

        Map<String, Object> sessionHash = new HashMap<>();
        session.getAttributeNames().forEach(attrName ->
                sessionHash.put(attrName, session.getAttribute(attrName)));

        redisTemplate.opsForHash().putAll(sessionKey, sessionHash);

        Duration remainingTime = session.getMaxInactiveInterval();
        String expiresKey = getExpiresKey(sessionId);
        redisTemplate.opsForValue().set(expiresKey, "", remainingTime);

        redisTemplate.expire(sessionKey, remainingTime);

        Object memberId = session.getAttribute(PRINCIPAL_NAME_INDEX_NAME);
        if (memberId != null) {
            String principalIndexKey = getPrincipalIndexKey(memberId.toString());
            redisTemplate.opsForSet().add(principalIndexKey, sessionId);
            redisTemplate.expire(principalIndexKey, remainingTime);

            log.debug("사용자 ID: {}의 세션 저장 완료. 세션 ID: {}", memberId, sessionId);
        }
    }

    @Override
    public MapSession findById(String id) {
        String sessionKey = getSessionKey(id);
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(sessionKey);

        if (entries.isEmpty()) {
            log.debug("세션을 찾을 수 없습니다. 세션 ID: {}", id);
            return null;
        }

        MapSession session = new MapSession(id);

        entries.forEach((key, value) -> {
            if (key instanceof String) {
                session.setAttribute((String) key, value);
            }
        });

        if (isSessionExpired(session)) {
            deleteById(id);
            return null;
        }

        log.debug("세션 조회 완료. 세션 ID: {}, 세션 속성: {}", id, session.getAttributeNames());
        return session;
    }

    @Override
    public void deleteById(String id) {
        log.debug("세션 삭제 시도. 세션 ID: {}", id);

        var session = findById(id);
        if (session != null) {
            var memberId = session.getAttribute(PRINCIPAL_NAME_INDEX_NAME);

            // 1. 세션 키 삭제
            String sessionKey = getSessionKey(id);
            redisTemplate.delete(sessionKey);

            // 2. 만료 키 삭제
            String expiresKey = getExpiresKey(id);
            redisTemplate.delete(expiresKey);

            // 3. 인덱스에서 세션 제거
            if (memberId != null) {
                String principalIndexKey = getPrincipalIndexKey(memberId.toString());
                redisTemplate.opsForSet().remove(principalIndexKey, id);
            }

            // 4. 세션 관련된 모든 추가 키 삭제
            String sessionPattern = SESSION_KEY_PREFIX + id + "*";
            Set<String> sessionKeys = redisTemplate.keys(sessionPattern);

            assert sessionKeys != null;
            if (!sessionKeys.isEmpty()) {
                redisTemplate.delete(sessionKeys);
            }

            log.info("세션 삭제 완료. 세션 ID: {}, 사용자 ID: {}", id, memberId);
        }
    }

    @Override
    public Map<String, MapSession> findByIndexNameAndIndexValue(String indexName, String indexValue) {
        if (!PRINCIPAL_NAME_INDEX_NAME.equals(indexName)) {
            return Collections.emptyMap();
        }

        String principalIndexKey = getPrincipalIndexKey(indexValue);
        Set<Object> sessionIds = redisTemplate.opsForSet().members(principalIndexKey);

        if (sessionIds == null || sessionIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return sessionIds.stream()
                .map(String.class::cast)
                .map(this::findById)
                .filter(Objects::nonNull)
                .filter(session -> !isSessionExpired(session))
                .collect(Collectors.toMap(MapSession::getId, session -> session));
    }

    @Override
    public void deleteAllByIndex(String memberId) {
        log.debug("사용자의 모든 세션 삭제 시도. 사용자 ID: {}", memberId);

        String principalIndexKey = getPrincipalIndexKey(memberId);
        Set<Object> sessionIds = redisTemplate.opsForSet().members(principalIndexKey);

        if (sessionIds != null && !sessionIds.isEmpty()) {
            sessionIds.stream()
                    .map(String.class::cast)
                    .forEach(sessionId -> {
                        String sessionKey = getSessionKey(sessionId);
                        String expiresKey = getExpiresKey(sessionId);

                        List<String> keysToDelete = Arrays.asList(sessionKey, expiresKey);
                        redisTemplate.delete(keysToDelete);

                        log.debug("세션 키 삭제 완료. 세션 ID: {}", sessionId);
                    });

            redisTemplate.delete(principalIndexKey);

            log.info("사용자의 모든 세션 삭제 완료. 사용자 ID: {}, 삭제된 세션 수: {}",
                    memberId, sessionIds.size());
        } else {
            log.debug("삭제할 세션이 없습니다. 사용자 ID: {}", memberId);
        }
    }

    private String getSessionKey(String sessionId) {
        return SESSION_KEY_PREFIX + sessionId;
    }

    private String getExpiresKey(String sessionId) {
        return EXPIRES_KEY_PREFIX + sessionId;
    }

    private String getPrincipalIndexKey(String principalName) {
        return PRINCIPAL_NAME_INDEX_PREFIX + principalName;
    }

    private boolean isSessionExpired(MapSession session) {
        Instant now = Instant.now();
        return session.getLastAccessedTime().plus(session.getMaxInactiveInterval()).isBefore(now);
    }
}