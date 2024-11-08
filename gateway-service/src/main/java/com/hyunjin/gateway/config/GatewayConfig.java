package com.hyunjin.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // [인증 서비스] - 세션/인증 관련 필터 필요
                .route("auth-service", r -> r
                        .path("/api/v1/auth/**", "/api/v1/member/**", "/api/v1/email/**")
                        .filters(f -> f.removeRequestHeader("Cookie"))
                        .uri("lb://member-auth-service"))
                // [상품 서비스] - 인증 불필요
                .route("product-service", r -> r
                        .path("/api/v1/product/**")
                        .uri("lb://product-service"))
                // [주문 서비스] - 인증 필요
                .route("order-service", r -> r
                        .path("/api/v1/orders/**", "/api/v1/wishlist/**")
                        .uri("lb://order-service"))
                .build();
    }
}