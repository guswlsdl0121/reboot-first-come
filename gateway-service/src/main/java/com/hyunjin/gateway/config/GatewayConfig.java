package com.hyunjin.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/api/auth/**", "/api/member/**", "/api/email/**")
                        .filters(f -> f
                                .preserveHostHeader()
                                .removeRequestHeader("Cookie")
                                .removeRequestHeader("Set-Cookie"))
                        .uri("lb://member-auth-service"))
                .build();
    }
}