package com.hyunjin.session;


import com.hyunjin.session.exception.SessionException;
import com.hyunjin.session.repository.CustomSessionRepository;
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

    // [세션] : 인증 정보 기반 신규 세션 생성 및 저장
    public String createSession(SecurityContext securityContext) {
        try {
            // Step 1: 세션 생성 및 인증 정보 바인딩
            MapSession session = sessionRepository.createSession();
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);
            session.setAttribute(PRINCIPAL_NAME_INDEX_NAME, securityContext.getAuthentication().getName());

            // Step 2: 세션 저장소에 세션 저장
            sessionRepository.save(session);
            return session.getId();

        } catch (Exception e) {
            log.error("[Session] 세션 생성 실패", e);
            throw new SessionException("세션 생성에 실패했습니다");
        }
    }

    // [세션] : 단일 세션 제거
    public void removeSession(String sessionId) {
        try {
            sessionRepository.deleteById(sessionId);
        } catch (Exception e) {
            log.error("[Session] 세션 삭제 실패. sessionId: {}", sessionId, e);
            throw new SessionException("세션 삭제에 실패했습니다");
        }
    }

    // [세션] : 사용자 관련 전체 세션 제거
    public void removeAllUserSessions(Integer memberId) {
        try {
            sessionRepository.deleteAllByIndex(memberId.toString());
        } catch (Exception e) {
            log.error("[Session] 사용자 세션 삭제 실패. memberId: {}", memberId, e);
            throw new SessionException("사용자의 모든 세션 삭제에 실패했습니다");
        }
    }
}