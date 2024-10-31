package com.hyunjin.member.usecase;

import com.hyunjin.entity.Member;
import com.hyunjin.entity.Role;
import com.hyunjin.member.dto.request.SignupRequest;
import com.hyunjin.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberAppender {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member newMember(SignupRequest request) {
        Member newMember = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .lastPasswordUpdated(LocalDateTime.now())
                .role(Role.ROLE_UNVERIFIED)
                .build();

        return memberRepository.save(newMember);
    }

}
