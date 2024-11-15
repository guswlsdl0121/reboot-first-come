package com.hyunjin.gateway.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                // CSRF 보호 비활성화 (API 게이트웨이이므로 불필요)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // Gateway에서는 세션을 생성하지 않고 Redis 세션만 확인
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())

                // 모든 요청 허용 (실제 인증은 AuthenticationFilter에서 처리)
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll()
                )
                .build();
    }
}