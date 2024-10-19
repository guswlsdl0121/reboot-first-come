package com.reboot_course.firstcome_system.auth.service;

import com.reboot_course.firstcome_system.auth.dto.request.SignupRequest;
import com.reboot_course.firstcome_system.member.entity.Member;
import com.reboot_course.firstcome_system.member.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder oneWayHashEncoder;
    private final MemberRepository memberRepository;

    public int signUp(@Valid SignupRequest request) {
        Member member = Member.builder()
                .name(request.getName())
                .password(oneWayHashEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .lastPasswordUpdated(LocalDateTime.now())
                .build();

        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }
}