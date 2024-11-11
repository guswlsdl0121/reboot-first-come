package com.hyunjin.gateway.config.route;

import com.hyunjin.gateway.filter.AuthenticationFilter;
import com.hyunjin.gateway.filter.LoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RouteConfig {
    private final LoggingFilter loggingFilter;
    private final AuthenticationFilter authenticationFilter;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // [인증 서비스]
                .route("auth-service", r -> r
                        .path("/api/v1/auth/**", "/api/v1/member/**", "/api/v1/email/**")
                        .filters(f -> {
                            loggingFilter.apply().accept(f);
                            authenticationFilter.apply().accept(f);
                            return f;
                        })
                        .uri("lb://member-auth-service"))

                // [상품 서비스]
                .route("product-service", r -> r
                        .path("/api/v1/product/**")
                        .filters(f -> {
                            loggingFilter.apply().accept(f);
                            // 상품 서비스는 인증 필요 없음
                            return f;
                        })
                        .uri("lb://product-service"))

                // [주문 서비스]
                .route("order-service", r -> r
                        .path("/api/v1/orders/**", "/api/v1/wishlist/**")
                        .filters(f -> {
                            loggingFilter.apply().accept(f);
                            authenticationFilter.apply().accept(f);
                            return f;
                        })
                        .uri("lb://order-service"))
                .build();
    }
}
