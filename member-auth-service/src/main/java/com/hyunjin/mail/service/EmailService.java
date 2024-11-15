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

    public void sendCode(Integer memberId) {
        // Step 1: 사용자 이메일 조회 및 인증 코드 생성
        String email = memberFinder.fetchEmailById(memberId);
        String authCode = mailSender.generateAuthCode();

        try {
            // Step 2: 인증 코드 저장 및 이메일 발송
            mailCodeRepository.saveCode(email, authCode, mailSender.getExpirationMillis());
            mailSender.sendCode(email, authCode);
        } catch (AuthCodeException e) {
            // Step 3: 실패 시 저장된 코드 정리
            mailCodeRepository.removeCode(email);
            throw new AuthCodeException("인증메일 전송을 실패했습니다.");
        }
    }

    public void verifyCode(Integer memberId, String code) {
        // Step 1: 사용자 정보 조회
        Member member = memberFinder.fetchById(memberId);
        String email = member.getEmail();

        // Step 2: 인증 코드 검증 및 사용자 상태 업데이트
        mailCodeRepository.verifyCode(email, code);
        memberUpdater.verifyEmail(member);
    }
}