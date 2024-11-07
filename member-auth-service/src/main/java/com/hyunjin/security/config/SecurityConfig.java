package com.hyunjin.security.config;

import com.hyunjin.security.provider.AuthenticationproviderImpl;
import com.hyunjin.session.filter.GatewayAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final SecurityProperties securityProperties;
    private final AuthenticationproviderImpl authenticationproviderImpl;
    private final GatewayAuthenticationFilter gatewayAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String[] publicUrls = securityProperties.getPublicUrls();
        log.info("인증 없이 접근 가능한 URL 목록: {}", Arrays.toString(publicUrls));

        http
                // CSRF 보호 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // 세션 관리 설정
                .sessionManagement(session -> session
                        // 세션을 직접 생성하지 않고 Gateway에서 전달된 세션만 사용
                        .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                )

                // SecurityContext 설정
                .securityContext((securityContext) -> securityContext
                        .requireExplicitSave(false))

                // URL별 접근 권한 설정
                .authorizeHttpRequests(
                        authz -> authz
                                .requestMatchers(publicUrls).permitAll()
                                .anyRequest().authenticated())
                .authenticationProvider(authenticationproviderImpl)
                .addFilterBefore(gatewayAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}