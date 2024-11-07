package com.hyunjin.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Slf4j
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/api/v1/auth/**", "/api/v1/member/**", "/api/v1/email/**")
                        .filters(f -> f
                                .preserveHostHeader()
                                .filter((exchange, chain) -> {
                                    ServerHttpRequest request = exchange.getRequest();

                                    // GET이 아닌 요청에 대해서만 Cookie 헤더 제거
                                    if (!HttpMethod.GET.equals(request.getMethod())) {
                                        request = request.mutate()
                                                .headers(headers -> {
                                                    headers.remove("Cookie");
                                                    headers.remove("Set-Cookie");
                                                })
                                                .build();
                                        exchange = exchange.mutate().request(request).build();
                                    }

                                    log.debug("인증 서비스로 요청 라우팅 - Method: {}, Path: {}",
                                            request.getMethod(),
                                            request.getPath());

                                    return chain.filter(exchange);
                                }))
                        .uri("lb://member-auth-service"))
                .build();
    }
}