package com.hyunjin.mail.service;


import com.hyunjin.mail.exception.AuthCodeException;
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

    // 인증 코드 생성 및 이메일 발송
    public void sendCode(Integer memberId) {
        String email = memberFinder.fetchEmailById(memberId);
        String authCode = mailSender.generateAuthCode();

        try {
            // 인증 코드 저장 및 메일 발송
            mailCodeRepository.saveCode(email, authCode, mailSender.getExpirationMillis());
            mailSender.sendCode(email, authCode);
        } catch (AuthCodeException e) {
            // 실패 시 저장된 코드 제거
            mailCodeRepository.removeCode(email);
            throw new AuthCodeException("인증메일 전송을 실패했습니다.");
        }
    }

    // 이메일 인증 코드 검증
    public void verifyCode(Integer memberId, String code) {
        Member member = memberFinder.fetchById(memberId);
        String email = member.getEmail();

        // 인증 코드 검증 및 이메일 인증 처리
        mailCodeRepository.verifyCode(email, code);
        memberUpdater.verifyEmail(member);
    }
}