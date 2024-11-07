package com.hyunjin.mail.service;


import com.hyunjin.mail.exception.exception.AuthCodeException;
import com.hyunjin.mail.repository.MailCodeRepository;
import com.hyunjin.mail.smtp.MailSender;
import com.hyunjin.member.entity.Member;
import com.hyunjin.member.service.usecase.MemberFinder;
import com.hyunjin.member.service.usecase.MemberUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final MemberFinder memberFinder;
    private final MemberUpdater memberUpdater;
    private final MailSender mailSender;
    private final MailCodeRepository mailCodeRepository;

    public void sendCode(Integer memberId) {
        String email = memberFinder.fetchEmailById(memberId);
        String authCode = mailSender.generateAuthCode();
        try {
            mailCodeRepository.saveCode(email, authCode, mailSender.getExpirationMillis());
            mailSender.sendCode(email, authCode);
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