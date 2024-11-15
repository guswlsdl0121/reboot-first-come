package com.hyunjin.gateway.filter;

import com.hyunjin.gateway.config.security.SecurityProperties;
import com.hyunjin.gateway.constants.http.HttpHeader;
import com.hyunjin.gateway.exception.exception.AuthenticationException;
import com.hyunjin.gateway.exception.exception.SessionException;
import com.hyunjin.gateway.session.SessionManager;
import com.hyunjin.gateway.session.SessionProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;


@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter {
    private final SessionProperties sessionProperties;
    private final SecurityProperties securityProperties;
    private final SessionManager sessionManager;

    public Consumer<GatewayFilterSpec> apply() {
        return f -> f.filter((exchange, chain) -> {
            // step 1: 현재 요청의 경로 추출
            String path = exchange.getRequest().getPath().value();

            // step 2: 요청 경로가 공개 API인지 확인
            boolean isPublicPath = securityProperties.getPublicPaths().stream()
                    .anyMatch(pattern -> new AntPathMatcher().match(pattern, path));

            // step 3: 공개 API인 경우 인증 없이 처리
            if (isPublicPath) {
                return chain.filter(exchange);
            }

            // step 3-1: 비공개 API는 인증 처리 후 진행
            return authenticate(exchange, chain);
        });
    }

    private Mono<Void> authenticate(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // step 1: 세션 쿠키 존재 여부 확인
        List<HttpCookie> cookies = request.getCookies().get(sessionProperties.getCookie().getName());
        if (cookies == null || cookies.isEmpty()) {
            return Mono.error(new SessionException("세션이 필요합니다."));
        }

        try {
            // step 2: 세션ID로 인증 정보 조회 및 검증
            String sessionId = cookies.getFirst().getValue();
            SecurityContext securityContext = sessionManager.getSecurityContext(sessionId);
            Authentication auth = securityContext.getAuthentication();

            if (auth == null || !auth.isAuthenticated()) {
                return Mono.error(new AuthenticationException("유효하지 않은 인증정보입니다."));
            }

            // step 3: 세션ID와 사용자 정보를 요청 헤더에 추가
            ServerHttpRequest mutatedRequest = request.mutate()
                    .headers(headers -> {
                        headers.remove(HttpHeader.COOKIE);
                        headers.add(HttpHeader.AUTH_TOKEN, sessionId);
                        headers.add(HttpHeader.USER_ID, auth.getName());
                        headers.add(HttpHeader.USER_ROLES,
                                auth.getAuthorities().stream()
                                        .map(Object::toString)
                                        .collect(Collectors.joining(",")));
                    })
                    .build();

            // step 4: 인증이 완료된 요청을 다음 필터로 전달
            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception e) {
            log.error("인증 처리 중 오류 발생", e);
            return Mono.error(new AuthenticationException("인증 처리 중 오류가 발생했습니다."));
        }
    }
}