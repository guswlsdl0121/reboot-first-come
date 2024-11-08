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
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GatewayAuthenticationFilter extends OncePerRequestFilter {
    private static final String X_USER_ID = "X-User-Id";
    private static final String X_USER_ROLES = "X-User-Roles";
    private final SecurityProperties securityProperties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Step 1: 공개 URL인 경우 인증 절차 생략
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 2: 게이트웨이 인증 헤더 검증
        String userId = request.getHeader(X_USER_ID);
        String userRoles = request.getHeader(X_USER_ROLES);

        if (!isValidAuthHeaders(userId, userRoles)) {
            log.warn("[인증 실패] 요청 URI: {}, 인증 헤더 누락 - userId: {}, roles: {}",
                    request.getRequestURI(), userId, userRoles);
            sendUnauthorizedResponse(response, "인증 정보가 누락되었습니다.");
            return;
        }

        try {
            // Step 3: 기존 인증 확인 및 재사용
            Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
            if (isValidAuthentication(existingAuth)) {
                log.debug("[인증 성공] 기존 인증 정보 재사용 - userId: {}", existingAuth.getName());
                filterChain.doFilter(request, response);
                return;
            }

            // Step 4: 권한 정보 변환 및 인증 객체 생성
            List<SimpleGrantedAuthority> authorities = convertToAuthorities(userRoles);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);

            // Step 5: Security Context 설정
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);

            // Step 6: 최종 인증 확인
            if (context.getAuthentication() == null) {
                log.error("[인증 실패] Security Context 생성 실패 - userId: {}", userId);
                sendUnauthorizedResponse(response, "인증에 실패했습니다.");
                return;
            }

            log.debug("[인증 성공] 새로운 인증 정보 생성 - userId: {}, roles: {}", userId, userRoles);
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // Step 7: 예외 처리
            log.error("[인증 오류] 인증 처리 중 예외 발생 - userId: {}", userId, e);
            SecurityContextHolder.clearContext();
            sendUnauthorizedResponse(response, "인증 처리 중 오류가 발생했습니다.");
        }
    }

    private boolean isValidAuthHeaders(String userId, String userRoles) {
        return StringUtils.hasText(userId) && StringUtils.hasText(userRoles);
    }

    private boolean isValidAuthentication(Authentication auth) {
        return auth != null && auth.isAuthenticated();
    }

    private List<SimpleGrantedAuthority> convertToAuthorities(String userRoles) {
        return Arrays.stream(userRoles.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(String.format("{\"message\":\"%s\"}", message));
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return Arrays.stream(securityProperties.getPublicUrls())
                .anyMatch(pattern -> pathMatcher.match(pattern, requestURI));
    }

}