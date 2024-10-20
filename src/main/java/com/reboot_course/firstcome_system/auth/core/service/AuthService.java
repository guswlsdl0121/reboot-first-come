package com.reboot_course.firstcome_system.auth.core.service;

import com.reboot_course.firstcome_system.auth.core.dto.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;

    public String login(LoginRequest loginRequest, HttpServletRequest request) {
        try {
            // 1. 인증객체 생성
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            // 2. 컨텍스트 홀더에 인증객체 저장
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            // 3. 위에서 만든 컨텍스트를 세션에 저장
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

            // 4. 세션 ID 반환
            return session.getId();
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed", e);
        }
    }

    public void logout(HttpServletRequest request) {
        // 1. 세션에서 비활성화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 2. 컨텍스트 홀더 비우기
        SecurityContextHolder.clearContext();
    }
}