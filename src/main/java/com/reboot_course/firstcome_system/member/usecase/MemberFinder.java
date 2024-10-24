package com.reboot_course.firstcome_system.member.usecase;

import com.reboot_course.firstcome_system.member.entity.Member;
import com.reboot_course.firstcome_system.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberFinder {
    private final MemberRepository memberRepository;

    @Transactional
    public Member fetchByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(String.format("해당 사용자를 찾을 수 없습니다. (email : %s)", email)));
    }
}
