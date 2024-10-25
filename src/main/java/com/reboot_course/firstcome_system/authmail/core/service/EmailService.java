package com.reboot_course.firstcome_system.authmail.core.service;

import com.reboot_course.firstcome_system.authmail.mail.exception.exception.AuthCodeException;
import com.reboot_course.firstcome_system.authmail.mail.AuthMailManager;
import com.reboot_course.firstcome_system.authmail.core.repository.MailCodeRepository;
import com.reboot_course.firstcome_system.member.entity.Member;
import com.reboot_course.firstcome_system.member.usecase.MemberFinder;
import com.reboot_course.firstcome_system.member.usecase.MemberUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final MemberFinder memberFinder;
    private final MemberUpdater memberUpdater;
    private final AuthMailManager authMailManager;
    private final MailCodeRepository mailCodeRepository;

    public void sendCode(Integer memberId) {
        String email = memberFinder.fetchEmailById(memberId);
        String authCode = authMailManager.generateAuthCode();
        try {
            mailCodeRepository.saveCode(email, authCode, authMailManager.getExpirationMillis());
            authMailManager.sendCode(email, authCode);
        } catch (AuthCodeException e) {
            mailCodeRepository.removeCode(email);
            throw new AuthCodeException("인증메일 전송을 실패했습니다.");
        }
    }

    public void verifyCode(Integer memberId, String code) {
        Member member = memberFinder.fetchById(memberId);
        String email = member.getEmail();
        mailCodeRepository.verifyCode(email, code);

        memberUpdater.verifyEmail(member);
    }
}