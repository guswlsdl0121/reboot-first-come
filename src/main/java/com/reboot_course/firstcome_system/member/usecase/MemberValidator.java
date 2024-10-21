package com.reboot_course.firstcome_system.member.usecase;

import com.reboot_course.firstcome_system.common.exception.exception.DuplicatedException;
import com.reboot_course.firstcome_system.member.dto.request.ChangePasswordRequest;
import com.reboot_course.firstcome_system.member.repository.MemberRepository;
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

    /*
     * @param memberPassword 데이터베이스에 저장된 회원의 암호화된 비밀번호
     * @param currentPassword 사용자가 입력한 현재 비밀번호
     * @throws IllegalArgumentException 비밀번호가 일치하지 않을 경우
     */
    public void matchPassword(String memberPassword, String currentPassword) {
        if (!passwordEncoder.matches(currentPassword, memberPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
