package com.hyunjin.session.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
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
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String userId = request.getHeader("X-User-Id");
        String userRoles = request.getHeader("X-User-Roles");

        if (userId != null && userRoles != null) {
            try {
                Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
                if (existingAuth == null || !existingAuth.isAuthenticated()) {
                    List<SimpleGrantedAuthority> authorities = Arrays.stream(userRoles.split(","))
                            .map(String::trim)
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userId, null, authorities);
                    SecurityContext context = SecurityContextHolder.getContext();
                    context.setAuthentication(authentication);

                    log.debug("Authenticating user {} with roles {}", userId, userRoles);
                }
            } catch (Exception e) {
                log.error("Authentication failed", e);
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return publicPaths.stream()
                .anyMatch(path -> request.getServletPath().startsWith(path));
    }
}