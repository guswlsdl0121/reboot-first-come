package com.reboot_course.firstcome_system.auth.session.service;

import com.reboot_course.firstcome_system.auth.session.dto.request.LoginRequest;
import com.reboot_course.firstcome_system.common.CommonResponse;
import com.reboot_course.firstcome_system.member.entity.Member;
import com.reboot_course.firstcome_system.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.MapSessionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {
    private static final String USER_SESSION_KEY = "USER";

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MapSessionRepository sessionRepository;

    public CommonResponse<String> login(LoginRequest request, HttpSession session) {
        try {
            Member member = authenticate(request.getEmail(), request.getPassword());
            session.setAttribute(USER_SESSION_KEY, member.getEmail());
            return CommonResponse.success("로그인에 성공했습니다.", member.getEmail());
        } catch (Exception e) {
            return CommonResponse.fail(e.getMessage());
        }
    }


    public void logout(HttpSession session) {
        session.invalidate();
    }

    private Member authenticate(String email, String password) {
        return memberRepository.findByEmail(email)
                .filter(member -> passwordEncoder.matches(password, member.getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
    }

    public String getCurrentUser(String sessionId) {
        return sessionRepository
                .findById(sessionId)
                .getAttribute(USER_SESSION_KEY)
                .toString();
    }
}