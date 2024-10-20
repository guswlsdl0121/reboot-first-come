package com.reboot_course.firstcome_system.member.service;

import com.reboot_course.firstcome_system.auth.core.service.AuthService;
import com.reboot_course.firstcome_system.member.dto.request.ChangePasswordRequest;
import com.reboot_course.firstcome_system.member.dto.request.SignupRequest;
import com.reboot_course.firstcome_system.member.entity.Member;
import com.reboot_course.firstcome_system.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public int signUp(SignupRequest request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use");
        }

        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .lastPasswordUpdated(LocalDateTime.now())
                .build();

        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        if (!passwordEncoder.matches(request.currentPassword(), member.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        String newEncodedPassword = passwordEncoder.encode(request.newPassword());
        member.changePassword(newEncodedPassword);

        memberRepository.save(member);

        // 모든 세션에서 로그아웃
        authService.logoutAllSessions(email);
    }
}
