package com.hyunjin.auth.session.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.MapSession;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

@Slf4j
@Primary
@Component
public class RedisIndexSessionRepository implements CustomSessionRepository {
    private static final String SESSION_PREFIX = "spring:session:";
    private static final String USER_SESSIONS_PREFIX = "spring:session:index:";

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
        String sessionKey = getSessionKey(session.getId());
        String memberId = session.getAttribute(PRINCIPAL_NAME_INDEX_NAME);

        Map<String, Object> sessionMap = new HashMap<>();
        for (String attrName : session.getAttributeNames()) {
            sessionMap.put(attrName, session.getAttribute(attrName));
        }

        redisTemplate.opsForHash().putAll(sessionKey, sessionMap);
        redisTemplate.expire(sessionKey, defaultMaxInactiveInterval);

        if (memberId != null) {
            String indexKey = getUserSessionsKey(memberId);
            redisTemplate.opsForSet().add(indexKey, session.getId());
            redisTemplate.expire(indexKey, defaultMaxInactiveInterval);
        }
    }

    @Override
    public MapSession findById(String id) {
        String sessionKey = getSessionKey(id);
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(sessionKey);

        if (entries.isEmpty()) {
            return null;
        }

        MapSession session = new MapSession(id);
        entries.forEach((key, value) -> {
            if (key instanceof String) {
                session.setAttribute(key.toString(), value);
            }
        });

        return session;
    }

    @Override
    public Map<String, MapSession> findByIndexNameAndIndexValue(String indexName, String indexValue) {
        if (!PRINCIPAL_NAME_INDEX_NAME.equals(indexName)) {
            return Collections.emptyMap();
        }

        String indexKey = getUserSessionsKey(indexValue);
        Set<Object> sessionIds = redisTemplate.opsForSet().members(indexKey);

        if (sessionIds == null) {
            return Collections.emptyMap();
        }

        Map<String, MapSession> sessions = new HashMap<>();
        for (Object sessionId : sessionIds) {
            MapSession session = findById((String) sessionId);
            if (session != null) {
                sessions.put((String) sessionId, session);
            }
        }

        return sessions;
    }

    @Override
    public void deleteById(String id) {
        MapSession session = findById(id);
        if (session != null) {
            String memberId = session.getAttribute(PRINCIPAL_NAME_INDEX_NAME);
            String sessionKey = getSessionKey(id);
            String indexKey = getUserSessionsKey(memberId);

            redisTemplate.delete(sessionKey);
            if (memberId != null) {
                redisTemplate.opsForSet().remove(indexKey, id);
            }
        }
    }

    @Override
    public void deleteAllByIndex(String memberId) {
        String indexKey = getUserSessionsKey(memberId);
        Set<Object> sessionIds = redisTemplate.opsForSet().members(indexKey);

        if (sessionIds != null && !sessionIds.isEmpty()) {
            for (Object sessionId : sessionIds) {
                redisTemplate.delete(getSessionKey((String) sessionId));
            }
            redisTemplate.delete(indexKey);
        }
    }

    private String getSessionKey(String sessionId) {
        return SESSION_PREFIX + sessionId;
    }

    private String getUserSessionsKey(String memberId) {
        return USER_SESSIONS_PREFIX + PRINCIPAL_NAME_INDEX_NAME + ":" + memberId;
    }
}