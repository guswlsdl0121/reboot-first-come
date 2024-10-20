package com.reboot_course.firstcome_system.member.service;

import com.reboot_course.firstcome_system.auth.session.dto.request.SignupRequest;
import com.reboot_course.firstcome_system.member.entity.Member;
import com.reboot_course.firstcome_system.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
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
}
