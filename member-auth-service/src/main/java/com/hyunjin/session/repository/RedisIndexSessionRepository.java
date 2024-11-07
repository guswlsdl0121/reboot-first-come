package com.hyunjin.session.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.MapSession;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
public class RedisIndexSessionRepository implements CustomSessionRepository {
    private static final String BASE_PREFIX = "spring:session:";
    private static final String SESSIONS_PREFIX = BASE_PREFIX + "sessions:";
    private static final String SESSION_KEY_PREFIX = SESSIONS_PREFIX + "session:";
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
        Object memberId = session.getAttribute(PRINCIPAL_NAME_INDEX_NAME);

        if (memberId == null) {
            return;
        }

        Map<String, MapSession> existingSessions = findByIndexNameAndIndexValue(
                PRINCIPAL_NAME_INDEX_NAME,
                memberId.toString()
        );
        existingSessions.keySet().forEach(this::deleteById);

        Map<String, Object> sessionData = new HashMap<>();
        Object securityContext = session.getAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContext != null) {
            sessionData.put("SPRING_SECURITY_CONTEXT", securityContext);
        }
        sessionData.put(PRINCIPAL_NAME_INDEX_NAME, memberId);
        sessionData.put("lastAccessedTime", System.currentTimeMillis());

        String sessionKey = getSessionKey(sessionId);
        redisTemplate.opsForHash().putAll(sessionKey, sessionData);
        redisTemplate.expire(sessionKey, session.getMaxInactiveInterval());

        String principalIndexKey = getPrincipalIndexKey(memberId.toString());
        redisTemplate.opsForSet().add(principalIndexKey, sessionId);
        redisTemplate.expire(principalIndexKey, session.getMaxInactiveInterval());

        log.debug("Session operation completed for ID: {}", sessionId);
    }

    @Override
    public MapSession findById(String id) {
        String sessionKey = getSessionKey(id);
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(sessionKey);

        if (entries.isEmpty()) {
            return null;
        }

        MapSession session = new MapSession(id);
        session.setMaxInactiveInterval(defaultMaxInactiveInterval);

        // 저장된 세션 데이터 복원
        entries.forEach((key, value) -> {
            if (key instanceof String) {
                session.setAttribute((String) key, value);
            }
        });

        log.debug("세션 조회 완료. 세션 ID: {}", id);
        return session;
    }

    @Override
    public void deleteById(String id) {
        String sessionKey = getSessionKey(id);
        Map<Object, Object> sessionData = redisTemplate.opsForHash().entries(sessionKey);

        if (!sessionData.isEmpty()) {
            Object memberId = sessionData.get(PRINCIPAL_NAME_INDEX_NAME);
            if (memberId != null) {
                String principalIndexKey = getPrincipalIndexKey(memberId.toString());
                redisTemplate.opsForSet().remove(principalIndexKey, id);
            }
            redisTemplate.delete(sessionKey);

            log.debug("세션 삭제 완료. 세션 ID: {}, 사용자 ID: {}", id, memberId);
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
                .collect(Collectors.toMap(MapSession::getId, session -> session));
    }

    @Override
    public void deleteAllByIndex(String memberId) {
        String principalIndexKey = getPrincipalIndexKey(memberId);
        Set<Object> sessionIds = redisTemplate.opsForSet().members(principalIndexKey);

        if (sessionIds != null && !sessionIds.isEmpty()) {
            sessionIds.stream()
                    .map(String.class::cast)
                    .forEach(this::deleteById);

            redisTemplate.delete(principalIndexKey);

            log.info("사용자의 모든 세션 삭제 완료. 사용자 ID: {}, 삭제된 세션 수: {}",
                    memberId, sessionIds.size());
        }
    }

    private String getSessionKey(String sessionId) {
        return SESSION_KEY_PREFIX + sessionId;
    }

    private String getPrincipalIndexKey(String principalName) {
        return PRINCIPAL_NAME_INDEX_PREFIX + principalName;
    }
}