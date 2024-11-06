package com.hyunjin.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // GET 요청을 위한 별도 라우트
                .route("auth-service-get", r -> r
                        .path("/api/auth/**", "/api/member/**", "/api/email/**")
                        .and()
                        .method(HttpMethod.GET)
                        .filters(f -> f
                                .preserveHostHeader()
                                .filter((exchange, chain) -> {
                                    log.debug("GET Request reached gateway route. Path: {}",
                                            exchange.getRequest().getPath());
                                    return chain.filter(exchange);
                                }))
                        .uri("lb://member-auth-service"))
                // POST/PUT/DELETE 등 다른 요청을 위한 라우트
                .route("auth-service-others", r -> r
                        .path("/api/auth/**", "/api/member/**", "/api/email/**")
                        .and()
                        .method(HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH)
                        .filters(f -> f
                                .preserveHostHeader()
                                .removeRequestHeader("Cookie")
                                .removeRequestHeader("Set-Cookie")
                                .modifyRequestBody(String.class, String.class,
                                        (exchange, s) -> {
                                            log.debug("Other Request reached gateway route. Path: {}",
                                                    exchange.getRequest().getPath());
                                            return Mono.just(s);
                                        }))
                        .uri("lb://member-auth-service"))
                .build();
    }
}