package com.hyunjin.gateway.filter;

import com.hyunjin.gateway.constants.AuthConstants;
import com.hyunjin.gateway.constants.RedisFields;
import com.hyunjin.gateway.constants.RedisKeys;
import com.hyunjin.gateway.exception.exception.SessionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class SessionFilter implements GlobalFilter, Ordered {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Step 1: 요청 경로 검증
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        // Step 2: 공개 API 확인
        if (AuthConstants.PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        // Step 3: 세션 ID 추출
        List<String> authHeaders = request.getHeaders().get(AuthConstants.X_AUTH_TOKEN);
        String sessionId = authHeaders != null && !authHeaders.isEmpty() ? authHeaders.getFirst() : null;
        if (sessionId == null) {
            return Mono.error(new SessionException("세션 정보가 없습니다"));
        }

        try {
            // Step 4: 세션 데이터 검증
            Map<Object, Object> sessionData = validateSession(sessionId);

            // Step 5: Security Context 생성
            SecurityContext securityContext = createSecurityContext(sessionData);

            // Step 6: 세션 처리
            return processSession(sessionId, securityContext, exchange, chain);
        } catch (Exception e) {
            log.error("세션 처리 중 오류 발생", e);
            return Mono.error(new SessionException("세션 처리 중 오류가 발생했습니다"));
        }
    }

    private Map<Object, Object> validateSession(String sessionId) {
        // Step 1: Redis에서 세션 데이터 조회
        String sessionKey = RedisKeys.getSessionKey(sessionId);
        Map<Object, Object> sessionData = redisTemplate.opsForHash().entries(sessionKey);

        // Step 2: 세션 데이터 유효성 검증
        if (sessionData.isEmpty() || !sessionData.containsKey(RedisFields.PRINCIPAL_NAME)) {
            throw new SessionException("유효하지 않은 세션");
        }

        return sessionData;
    }

    private SecurityContext createSecurityContext(Map<Object, Object> sessionData) {
        SecurityContext securityContext = (SecurityContext) sessionData.get(RedisFields.SECURITY_CONTEXT);
        if (securityContext == null || securityContext.getAuthentication() == null) {
            throw new SessionException("유효하지 않은 인증 정보");
        }

        return securityContext;
    }

    private Mono<Void> processSession(String sessionId, SecurityContext securityContext,
                                      ServerWebExchange exchange, GatewayFilterChain chain) {
        // Step 1: 인증 정보를 헤더에 추가
        Authentication auth = securityContext.getAuthentication();
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header(AuthConstants.X_USER_ID, auth.getName())
                .header(AuthConstants.X_USER_ROLES, String.join(",",
                        auth.getAuthorities().stream()
                                .map(Object::toString)
                                .toList()))
                .build();

        // Step 2: 세션 접근 시간 업데이트
        String sessionKey = RedisKeys.getSessionKey(sessionId);
        redisTemplate.opsForHash().put(sessionKey, RedisFields.LAST_ACCESSED_TIME, System.currentTimeMillis());
        redisTemplate.expire(sessionKey, Duration.ofMinutes(30));

        // Step 3: 수정된 요청으로 필터 체인 계속 실행
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        return -1;
    }
}