package com.hyunjin.member.service.usecase;

import com.hyunjin.member.entity.Member;
import com.hyunjin.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberUpdater {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void updatePassword(Member member, String newPassword) {
        String newEncodedPassword = passwordEncoder.encode(newPassword);
        member.changePassword(newEncodedPassword);

        memberRepository.save(member);
    }

    public void verifyEmail(Member member) {
        member.verifyEmail();
        memberRepository.save(member);
    }

}
