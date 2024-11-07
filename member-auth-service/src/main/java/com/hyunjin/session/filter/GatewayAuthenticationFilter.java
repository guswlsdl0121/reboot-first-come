package com.hyunjin.session.filter;

import com.hyunjin.security.config.SecurityProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class GatewayAuthenticationFilter extends OncePerRequestFilter {
    private final SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (!shouldNotFilter(request)) {
            String userId = request.getHeader("X-User-Id");
            String userRoles = request.getHeader("X-User-Roles");

            if (userId == null || userRoles == null) {
                log.warn("인증 헤더 누락. uri: {}", request.getRequestURI());
                sendUnauthorizedResponse(response, "인증 정보가 누락되었습니다.");
                return;
            }

            try {
                Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

                if (existingAuth != null && existingAuth.isAuthenticated()) {
                    filterChain.doFilter(request, response);
                    return;
                }

                List<SimpleGrantedAuthority> authorities = Arrays.stream(userRoles.split(","))
                        .map(String::trim)
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);
                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(authentication);

                log.info("게이트웨이 인증 처리 완료. userId: {}, roles: {}", userId, userRoles);

            } catch (Exception e) {
                log.error("게이트웨이 인증 처리 실패. userId: {}", userId, e);
                SecurityContextHolder.clearContext();
                sendUnauthorizedResponse(response, "인증 처리 중 오류가 발생했습니다.");
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                log.error("인증 컨텍스트 생성 실패. userId: {}", userId);
                sendUnauthorizedResponse(response, "인증에 실패했습니다.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(String.format("{\"message\":\"%s\"}", message));
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return Arrays.stream(securityProperties.getPublicUrls())
                .anyMatch(path -> request.getServletPath().startsWith(path));
    }
}