package com.hyunjin.gateway.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
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

        log.error("=== Session Filter Start ===");
        log.error("Request Path: {}", path);
        log.error("Request Headers: {}", request.getHeaders());

        if (isPublicPath(path)) {
            log.error("Public path detected - skipping session validation");
            return chain.filter(exchange);
        }

        String sessionId = getSessionId(request);
        if (sessionId == null) {
            log.error("Session ID is missing in request");
            return onError(exchange, "Missing session ID");
        }
        log.error("Session ID found: {}", sessionId);

        return validateSession(sessionId)
                .doOnNext(sessionData -> log.error("Session Data: {}", sessionData))
                .flatMap(sessionData -> {
                    if (sessionData == null) {
                        log.error("No session data found in Redis");
                        return onError(exchange, "Invalid session");
                    }

                    SecurityContext securityContext = extractSecurityContext(sessionData);
                    if (securityContext == null || securityContext.getAuthentication() == null) {
                        log.error("Invalid security context in session");
                        return onError(exchange, "Invalid security context");
                    }

                    Authentication auth = securityContext.getAuthentication();
                    log.error("User authenticated - Name: {}, Authorities: {}",
                            auth.getName(), auth.getAuthorities());

                    ServerHttpRequest mutatedRequest = request.mutate()
                            .header("X-User-Id", auth.getName())
                            .header("X-User-Roles", String.join(",",
                                    auth.getAuthorities().stream()
                                            .map(Object::toString)
                                            .toList()))
                            .build();

                    log.error("Request headers after mutation: {}", mutatedRequest.getHeaders());

                    updateSessionLastAccessedTime(sessionId);
                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                })
                .onErrorResume(e -> {
                    log.error("Error during session validation", e);
                    return onError(exchange, "Session validation failed");
                });
    }

    private boolean isPublicPath(String path) {
        return publicPaths.stream().anyMatch(path::startsWith);
    }

    private String getSessionId(ServerHttpRequest request) {
        List<String> authHeaders = request.getHeaders().get(X_AUTH_TOKEN);
        String sessionId = authHeaders != null && !authHeaders.isEmpty() ? authHeaders.get(0) : null;
        log.error("Extracted session ID from header: {}", sessionId);
        return sessionId;
    }

    private Mono<Map<Object, Object>> validateSession(String sessionId) {
        try {
            String sessionKey = SESSION_PREFIX + "session:" + sessionId;
            Map<Object, Object> sessionData = redisTemplate.opsForHash().entries(sessionKey);

            log.error("Redis key being checked: {}", sessionKey);
            log.error("Session data from Redis: {}", sessionData);

            if (sessionData.isEmpty()) {
                return Mono.empty();
            }

            // SecurityContext가 있다면 세션이 유효한 것으로 처리
            if (sessionData.containsKey(SPRING_SECURITY_CONTEXT)) {
                return Mono.just(sessionData);
            }

            return Mono.empty();
        } catch (Exception e) {
            log.error("Error while validating session", e);
            return Mono.error(e);
        }
    }

    private SecurityContext extractSecurityContext(Map<Object, Object> sessionData) {
        try {
            Object contextData = sessionData.get(SPRING_SECURITY_CONTEXT);
            log.error("Security context from session: {}", contextData);
            return (SecurityContext) contextData;
        } catch (Exception e) {
            log.error("Error extracting security context", e);
            return null;
        }
    }

    private void updateSessionLastAccessedTime(String sessionId) {
        try {
            String key = SESSION_PREFIX + sessionId;
            Map<Object, Object> sessionData = redisTemplate.opsForHash().entries(key);

            // 기존 데이터 유지하면서 lastAccessedTime만 업데이트
            if (!sessionData.isEmpty()) {
                sessionData.put("lastAccessedTime", System.currentTimeMillis());
                redisTemplate.opsForHash().putAll(key, sessionData);
                redisTemplate.expire(key, Duration.ofMinutes(30));
                log.debug("Updated session last accessed time for session: {}", sessionId);
            }
        } catch (Exception e) {
            log.error("Failed to update session last accessed time", e);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        log.error("Authentication error: {}", message);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}