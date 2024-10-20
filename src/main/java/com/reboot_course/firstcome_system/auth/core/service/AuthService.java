package com.reboot_course.firstcome_system.auth.core.service;

import com.reboot_course.firstcome_system.auth.core.dto.request.LoginRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    public String login(LoginRequest loginRequest, HttpSession session) {
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
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
            session.setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, authentication.getName());

            // 4. 세션 ID 반환
            return session.getId();
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed", e);
        }
    }

    public void logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
    }

    public void logoutAllSessions(String email) {
        log.info("Attempting to logout all sessions for user: {}", email);

        Map<String, ? extends Session> userSessions = sessionRepository.findByIndexNameAndIndexValue(
                FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, email);

        log.info("Found {} sessions for user: {}", userSessions.size(), email);

        for (Session session : userSessions.values()) {
            try {
                String sessionId = session.getId();
                log.info("Invalidating session: {}", sessionId);

                // 세션의 모든 속성 제거
                for (String attributeName : session.getAttributeNames()) {
                    session.removeAttribute(attributeName);
                }

                // 세션 만료 처리
                session.setMaxInactiveInterval(Duration.ZERO);

                // 세션 저장소에서 세션 삭제
                sessionRepository.deleteById(sessionId);

                log.info("Session invalidated and deleted: {}", sessionId);
            } catch (Exception e) {
                log.error("Error while invalidating session", e);
            }
        }

        // 세션 삭제 후 다시 확인
        Map<String, ? extends Session> remainingSessions = sessionRepository.findByIndexNameAndIndexValue(
                FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, email);
        log.info("Remaining sessions after logout: {}", remainingSessions.size());

        SecurityContextHolder.clearContext();
        log.info("SecurityContext cleared for current thread");
    }
}