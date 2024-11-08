package com.hyunjin.security;

import com.hyunjin.security.exception.AuthenticationFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityManager {
    private final AuthenticationManager authenticationManager;

    // [인증] : 사용자 인증 후 SecurityContext 생성
    public SecurityContext authenticate(String email, String password) {
        try {
            // Step 1: SecurityContext 및 인증 토큰 생성
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);

            // Step 2: 인증 수행 및 컨텍스트 설정
            Authentication authentication = authenticationManager.authenticate(authRequest);
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            return context;

        } catch (BadCredentialsException e) {
            log.error("[Security] 잘못된 인증 정보");
            throw new AuthenticationFailedException("인증 정보가 올바르지 않습니다");
        } catch (Exception e) {
            log.error("[Security] 인증 실패", e);
            throw new AuthenticationFailedException("인증 처리 중 오류가 발생했습니다");
        }
    }

    // [인증] : SecurityContext 초기화
    public void clearContext() {
        SecurityContextHolder.clearContext();
    }
}
