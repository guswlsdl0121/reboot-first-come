package com.reboot_course.firstcome_system.auth.core.service;

import com.reboot_course.firstcome_system.auth.core.dto.request.LoginRequest;
import com.reboot_course.firstcome_system.auth.session.repository.CustomSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.MapSession;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private static final String SPRING_SECURITY_CONTEXT_KEY = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
    private static final String PRINCIPAL_NAME_INDEX_NAME = FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME;

    private final AuthenticationManager authenticationManager;
    private final CustomSessionRepository sessionRepository;

    public String login(LoginRequest loginRequest) {
        try {
            // 1. 인증객체 생성
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            // 2. 인증객체로 SecurityContext를 생성 후 현재 쓰레드의 ContextHolder에 저장
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            // 3. 새 세션 생성 및 SecurityContext와 사용자 이메일 저장
            MapSession session = sessionRepository.createSession();
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
            session.setAttribute(PRINCIPAL_NAME_INDEX_NAME, authentication.getName());
            sessionRepository.save(session);

            // 4. 세션 ID 반환
            return session.getId();
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed", e);
        }
    }

    public void logout(String sessionId) {
        sessionRepository.deleteById(sessionId);
        SecurityContextHolder.clearContext();
    }

    public void logoutAllSessions(String email) {
        sessionRepository.deleteAllByIndex(email);
        SecurityContextHolder.clearContext();
    }
}