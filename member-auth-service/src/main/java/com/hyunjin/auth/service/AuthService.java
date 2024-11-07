package com.hyunjin.auth.service;

import com.hyunjin.auth.api.dto.request.LoginRequest;
import com.hyunjin.security.SecurityManager;
import com.hyunjin.session.SessionManager;
import com.hyunjin.session.exception.SessionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final SecurityManager securityManager;
    private final SessionManager sessionManager;

    public String login(LoginRequest loginRequest) {
        try {
            // Step 1: 인증 처리
            SecurityContext securityContext = securityManager.authenticate(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );

            // Step 2: 세션 생성
            String sessionId = sessionManager.createSession(securityContext);
            log.info("로그인 성공. email: {}", loginRequest.getEmail());

            return sessionId;

        } catch (SessionException e) {
            log.error("세션 생성 실패. email: {}", loginRequest.getEmail());
            securityManager.clearContext();
            throw e;
        }
    }

    public void logout(String sessionId) {
        // Step 1: 세션 제거
        sessionManager.removeSession(sessionId);

        // Step 2: 보안 컨텍스트 초기화
        securityManager.clearContext();
        log.info("로그아웃 처리 완료. sessionId: {}", sessionId);
    }

    public void logoutAllSessions(Integer memberId) {
        // Step 1: 모든 세션 제거
        sessionManager.removeAllUserSessions(memberId);

        // Step 2: 보안 컨텍스트 초기화
        securityManager.clearContext();
        log.info("전체 세션 로그아웃 처리 완료. memberId: {}", memberId);
    }
}