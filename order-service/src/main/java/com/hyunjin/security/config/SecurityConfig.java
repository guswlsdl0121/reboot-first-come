package com.hyunjin.security.config;

import com.hyunjin.security.filter.GatewayAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final GatewayAuthenticationFilter gatewayAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // OPTIONS 요청 허용
                        .requestMatchers(
                                "/api/v1/product/internal/**",
                                "/api/v1/orders/internal/**",
                                "/api/v1/member/internal/**"
                        ).permitAll()  // 구체적인 패턴 지정
                        .anyRequest().authenticated()
                )
                .addFilterBefore(gatewayAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // requestMatcher를 위한 빈 등록
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(
                        "/api/v1/product/internal/**",
                        "/api/v1/orders/internal/**",
                        "/api/v1/member/internal/**"
                );
    }
}