package com.hyunjin.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;


@Slf4j
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // [인증 서비스] - 세션/인증 관련 필터 필요
                .route("auth-service", r -> r
                        .path("/api/v1/auth/**", "/api/v1/member/**", "/api/v1/email/**")
                        .filters(f -> f
                                .removeRequestHeader("Cookie")
                                // 기존 헤더 유지를 위한 빈 헤더 추가
                                .addRequestHeader("X-Auth-Token", "")
                                .addRequestHeader("X-User-Id", "")
                                .addRequestHeader("X-User-Roles", "")
                                // 로깅 필터
                                .filter((exchange, chain) -> {
                                    log.debug("Request Headers: {}",
                                            exchange.getRequest().getHeaders());
                                    return chain.filter(exchange)
                                            .doOnSuccess(v -> log.debug("Response Headers: {}",
                                                    exchange.getResponse().getHeaders()));
                                }))
                        .uri("lb://member-auth-service"))

                // [상품 서비스] - 인증 불필요
                .route("product-service", r -> r
                        .path("/api/v1/product/**")
                        .filters(f -> f
                                .removeRequestHeader("Cookie")
                                // 기본 CORS 설정
                                .addResponseHeader("Access-Control-Allow-Origin", "*")
                                .addResponseHeader("Access-Control-Allow-Methods",
                                        "GET, POST, PUT, DELETE, OPTIONS"))
                        .uri("lb://product-service"))

                // [주문 서비스] - 인증 필요
                .route("order-service", r -> r
                        .path("/api/v1/orders/**", "/api/v1/wishlist/**")
                        .filters(f -> f
                                .removeRequestHeader("Cookie")
                                // 인증 헤더 유지
                                .addRequestHeader("X-Auth-Token", "")
                                .addRequestHeader("X-User-Id", "")
                                .addRequestHeader("X-User-Roles", ""))
                        .uri("lb://order-service"))

                // Global CORS configuration
                .route("global-cors", r -> r
                        .path("/**")
                        .and()
                        .method(HttpMethod.OPTIONS)
                        .filters(f -> f
                                .setStatus(200)
                                .addResponseHeader("Access-Control-Allow-Origin", "*")
                                .addResponseHeader("Access-Control-Allow-Methods",
                                        "GET, POST, PUT, DELETE, OPTIONS")
                                .addResponseHeader("Access-Control-Allow-Headers",
                                        "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With," +
                                                "If-Modified-Since,Cache-Control,Content-Type,Authorization," +
                                                "X-Auth-Token,X-User-Id,X-User-Roles")
                                .addResponseHeader("Access-Control-Max-Age", "3600"))
                        .uri("no://op"))
                .build();
    }
}