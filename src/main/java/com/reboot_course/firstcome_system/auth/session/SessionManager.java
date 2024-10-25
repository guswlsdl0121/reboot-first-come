package com.reboot_course.firstcome_system.auth.session;

import com.reboot_course.firstcome_system.auth.session.repository.CustomSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.MapSession;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionManager {
    private static final String SPRING_SECURITY_CONTEXT_KEY = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
    private static final String PRINCIPAL_NAME_INDEX_NAME = FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME;

    private final CustomSessionRepository sessionRepository;

    public String createSession(SecurityContext securityContext) {
        MapSession session = sessionRepository.createSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);
        session.setAttribute(PRINCIPAL_NAME_INDEX_NAME, securityContext.getAuthentication().getName());
        sessionRepository.save(session);

        return session.getId();
    }

    public void removeSession(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    public void removeAllUserSessions(Integer memberId) {
        sessionRepository.deleteAllByIndex(memberId.toString());
    }
}