package com.hyunjin.firstcome_system.auth.core.service;

import com.hyunjin.firstcome_system.auth.core.dto.request.LoginRequest;
import com.hyunjin.firstcome_system.auth.security.SecurityManager;
import com.hyunjin.firstcome_system.auth.session.SessionManager;
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
        SecurityContext securityContext = securityManager.authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        return sessionManager.createSession(securityContext);
    }

    public void logout(String sessionId) {
        sessionManager.removeSession(sessionId);
        securityManager.clearContext();
    }

    public void logoutAllSessions(Integer memberId) {
        sessionManager.removeAllUserSessions(memberId);
        securityManager.clearContext();
    }
}