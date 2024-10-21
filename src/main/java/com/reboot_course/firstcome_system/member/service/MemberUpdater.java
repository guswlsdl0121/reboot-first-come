package com.reboot_course.firstcome_system.member.service;

import com.reboot_course.firstcome_system.member.entity.Member;
import com.reboot_course.firstcome_system.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberUpdater {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void updatePassword(Member member, String newPassword) {
        String newEncodedPassword = passwordEncoder.encode(newPassword);
        member.changePassword(newEncodedPassword);

        memberRepository.save(member);
    }

}
