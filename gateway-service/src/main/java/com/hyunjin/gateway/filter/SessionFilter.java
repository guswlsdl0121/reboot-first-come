package com.hyunjin.gateway.filter;

import com.hyunjin.gateway.constants.AuthConstants;
import com.hyunjin.gateway.constants.RedisConstants;
import com.hyunjin.gateway.exception.exception.SessionException;
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

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        log.debug("세션 필터 처리 시작 - 요청 경로: {}", path);

        if (isPublicPath(path)) {
            log.debug("인증이 필요없는 공개 API 요청");
            return chain.filter(exchange);
        }

        String sessionId = getSessionId(request);
        if (sessionId == null) {
            log.warn("세션 ID가 요청에 없음");
            return Mono.error(new SessionException("세션 정보가 없습니다"));
        }

        return validateSession(sessionId)
                .flatMap(sessionData -> {
                    if (sessionData == null) {
                        log.warn("Redis에서 세션 정보를 찾을 수 없음: {}", sessionId);
                        return Mono.error(new SessionException("유효하지 않은 세션"));
                    }

                    SecurityContext securityContext = extractSecurityContext(sessionData);
                    if (securityContext == null || securityContext.getAuthentication() == null) {
                        log.warn("세션의 인증 정보가 유효하지 않음: {}", sessionId);
                        return Mono.error(new SessionException("유효하지 않은 인증 정보"));
                    }

                    Authentication auth = securityContext.getAuthentication();
                    log.debug("인증된 사용자 정보 - ID: {}, 권한: {}",
                            auth.getName(), auth.getAuthorities());

                    ServerHttpRequest mutatedRequest = request.mutate()
                            .header(AuthConstants.X_USER_ID, auth.getName())
                            .header(AuthConstants.X_USER_ROLES, String.join(",",
                                    auth.getAuthorities().stream()
                                            .map(Object::toString)
                                            .toList()))
                            .build();

                    updateSessionLastAccessedTime(sessionId);
                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                });
    }

    private boolean isPublicPath(String path) {
        return AuthConstants.PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private String getSessionId(ServerHttpRequest request) {
        List<String> authHeaders = request.getHeaders().get(AuthConstants.X_AUTH_TOKEN);
        return authHeaders != null && !authHeaders.isEmpty() ? authHeaders.getFirst() : null;
    }

    private Mono<Map<Object, Object>> validateSession(String sessionId) {
        try {
            String sessionKey = RedisConstants.SESSION_PREFIX + "session:" + sessionId;
            Map<Object, Object> sessionData = redisTemplate.opsForHash().entries(sessionKey);

            if (sessionData.isEmpty() || !sessionData.containsKey(RedisConstants.SECURITY_CONTEXT)) {
                return Mono.empty();
            }

            return Mono.just(sessionData);
        } catch (Exception e) {
            log.error("세션 검증 중 Redis 오류 발생", e);
            return Mono.error(e);
        }
    }

    private SecurityContext extractSecurityContext(Map<Object, Object> sessionData) {
        try {
            return (SecurityContext) sessionData.get(RedisConstants.SECURITY_CONTEXT);
        } catch (Exception e) {
            log.error("Security Context 추출 중 오류 발생", e);
            return null;
        }
    }

    private void updateSessionLastAccessedTime(String sessionId) {
        try {
            String key = RedisConstants.SESSION_PREFIX + sessionId;
            Map<Object, Object> sessionData = redisTemplate.opsForHash().entries(key);

            if (!sessionData.isEmpty()) {
                sessionData.put(RedisConstants.LAST_ACCESSED_TIME, System.currentTimeMillis());
                redisTemplate.opsForHash().putAll(key, sessionData);
                redisTemplate.expire(key, Duration.ofMinutes(30));
                log.debug("세션 접근 시간 업데이트 완료: {}", sessionId);
            }
        } catch (Exception e) {
            log.error("세션 접근 시간 업데이트 실패", e);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}