package com.hyunjin.gateway.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
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
    private static final String SESSION_PREFIX = "spring:session:sessions:";
    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
    private static final String PRINCIPAL_NAME_INDEX_NAME = "org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME";
    private static final String X_AUTH_TOKEN = "X-Auth-Token";

    private final List<String> publicPaths = List.of(
            "/api/auth/login",
            "/api/auth/logout",
            "/api/member/signup",
            "/api/email/verify-email"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        String sessionId = getSessionId(request);
        if (sessionId == null) {
            return onError(exchange, "Missing session ID", HttpStatus.UNAUTHORIZED);
        }

        return validateSession(sessionId)
                .flatMap(sessionData -> {
                    if (sessionData == null) {
                        return onError(exchange, "Invalid session", HttpStatus.UNAUTHORIZED);
                    }

                    SecurityContext securityContext = extractSecurityContext(sessionData);
                    if (securityContext == null || securityContext.getAuthentication() == null) {
                        return onError(exchange, "Invalid security context", HttpStatus.UNAUTHORIZED);
                    }

                    Authentication auth = securityContext.getAuthentication();
                    String userId = auth.getName();
                    String roles = String.join(",", auth.getAuthorities().stream()
                            .map(Object::toString)
                            .toList());

                    // 헤더만 전달하고 본문은 처리하지 않음
                    ServerHttpRequest mutatedRequest = request.mutate()
                            .header("X-User-Id", userId)
                            .header("X-User-Roles", roles)
                            .build();

                    updateSessionLastAccessedTime(sessionId);

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                });
    }

    private boolean isPublicPath(String path) {
        return publicPaths.stream().anyMatch(path::startsWith);
    }

    private String getSessionId(ServerHttpRequest request) {
        List<String> authHeaders = request.getHeaders().get(X_AUTH_TOKEN);
        return authHeaders != null && !authHeaders.isEmpty() ? authHeaders.get(0) : null;
    }

    private Mono<Map<Object, Object>> validateSession(String sessionId) {
        try {
            Map<Object, Object> sessionData = redisTemplate.opsForHash()
                    .entries(SESSION_PREFIX + sessionId);

            if (sessionData.isEmpty()) {
                return Mono.empty();
            }

            // Validate session expiration
            Long creationTime = (Long) sessionData.get("creationTime");
            Integer maxInactiveInterval = (Integer) sessionData.get("maxInactiveInterval");
            Long lastAccessedTime = (Long) sessionData.get("lastAccessedTime");

            if (creationTime == null || maxInactiveInterval == null || lastAccessedTime == null) {
                return Mono.empty();
            }

            long currentTime = System.currentTimeMillis();
            long expirationTime = lastAccessedTime + (maxInactiveInterval * 1000L);

            if (currentTime > expirationTime) {
                redisTemplate.delete(SESSION_PREFIX + sessionId);
                return Mono.empty();
            }

            return Mono.just(sessionData);
        } catch (Exception e) {
            log.error("Session validation error for session ID: {}", sessionId, e);
            return Mono.empty();
        }
    }

    private SecurityContext extractSecurityContext(Map<Object, Object> sessionData) {
        try {
            return (SecurityContext) sessionData.get(SPRING_SECURITY_CONTEXT);
        } catch (Exception e) {
            log.error("Failed to extract security context from session", e);
            return null;
        }
    }

    private void updateSessionLastAccessedTime(String sessionId) {
        try {
            String sessionKey = SESSION_PREFIX + sessionId;
            redisTemplate.opsForHash().put(sessionKey, "lastAccessedTime", System.currentTimeMillis());
            // Refresh expiration
            redisTemplate.expire(sessionKey, Duration.ofDays(redisTemplate.getExpire(sessionKey)));
        } catch (Exception e) {
            log.error("Failed to update session last accessed time", e);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        log.warn("Authentication error: {}", message);
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}