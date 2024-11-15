package com.hyunjin.gateway.session;

import com.hyunjin.gateway.constants.redis.RedisFields;
import com.hyunjin.gateway.constants.redis.RedisKeys;
import com.hyunjin.gateway.exception.exception.SessionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionManager {
    private final SessionProperties sessionProperties;
    private final RedisTemplate<String, Object> redisTemplate;

    public SecurityContext getSecurityContext(String sessionId) {
        // step 1: 세션키로 Redis에서 세션 데이터 조회
        String sessionKey = RedisKeys.getSessionKey(sessionId);
        Map<Object, Object> sessionData = redisTemplate.opsForHash().entries(sessionKey);

        // step 2: 세션 데이터 유효성 검증
        if (sessionData.isEmpty() || !sessionData.containsKey(RedisFields.SECURITY_CONTEXT)) {
            throw new SessionException("유효하지 않은 세션");
        }

        // step 3: SecurityContext 추출 및 검증
        SecurityContext securityContext = (SecurityContext) sessionData.get(RedisFields.SECURITY_CONTEXT);
        if (securityContext == null || securityContext.getAuthentication() == null) {
            throw new SessionException("유효하지 않은 인증 정보");
        }

        // step 4: 세션 접근 시간 갱신 및 만료 시간 연장
        redisTemplate.opsForHash().put(sessionKey, RedisFields.LAST_ACCESSED_TIME, System.currentTimeMillis());
        redisTemplate.expire(sessionKey, Duration.ofSeconds(sessionProperties.getTimeout()));

        return securityContext;
    }
}