package com.hyunjin.member.service.usecase;


import com.hyunjin.common.exception.exception.DuplicatedException;
import com.hyunjin.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberValidator {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void checkEmailExists(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicatedException(String.format("해당 이메일은 현재 사용중입니다. (%s)", email));
        }
    }

    public void matchPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
