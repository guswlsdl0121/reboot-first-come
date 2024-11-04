package com.hyunjin.auth.security.config;

import com.hyunjin.auth.security.provider.AuthenticationproviderImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final SecurityProperties securityProperties;
    private final AuthenticationproviderImpl authenticationproviderImpl;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)  // 최대 세션 수를 1개로 제한
                        .maxSessionsPreventsLogin(true)  // true: 새로운 로그인 차단, false: 기존 세션 만료
                )
                .securityContext((securityContext) -> securityContext
                        .requireExplicitSave(false))
                .authorizeHttpRequests(
                        authz -> authz
                                .requestMatchers(securityProperties.getPublicUrls()).permitAll()
                                .anyRequest().authenticated())
                .authenticationProvider(authenticationproviderImpl);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}