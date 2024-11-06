package com.hyunjin.auth.session;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class GatewayAuthenticationFilter extends OncePerRequestFilter {

    private final List<String> publicPaths = List.of(
            "/api/auth/login",
            "/api/auth/logout",
            "/api/member/signup",
            "/api/email/verify-email"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String userId = request.getHeader("X-User-Id");
        String userRoles = request.getHeader("X-User-Roles");

        log.debug("=== Gateway Auth Filter ===");
        log.debug("Request URI: {}", request.getRequestURI());
        log.debug("X-User-Id header: {}", userId);
        log.debug("X-User-Roles header: {}", userRoles);

        if (userId != null && userRoles != null) {
            try {
                List<SimpleGrantedAuthority> authorities = Arrays.stream(userRoles.split(","))
                        .map(String::trim)
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // SecurityContext 설정 후 상태 로깅
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                log.debug("=== Security Context After Setting ===");
                log.debug("Is authenticated: {}", auth.isAuthenticated());
                log.debug("Principal: {}", auth.getPrincipal());
                log.debug("Authorities: {}", auth.getAuthorities());

            } catch (Exception e) {
                log.error("Failed to process authentication headers", e);
                SecurityContextHolder.clearContext();
            }
        } else {
            log.debug("No authentication headers found");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return publicPaths.stream()
                .anyMatch(path -> request.getServletPath().startsWith(path));
    }
}