package com.hyunjin.mail.smtp;

import com.hyunjin.mail.exception.AuthCodeException;
import com.hyunjin.mail.smtp.config.MailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class MailSender {
    private final JavaMailSender emailSender;
    private final MailProperties mailProperties;

    // 6자리 랜덤 인증코드 생성
    public String generateAuthCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    // 이메일 발송
    public void sendCode(String to, String authCode) {
        // Step 2-1: 메일 메시지 구성
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailProperties.getUsername());
        message.setTo(to);
        message.setSubject("이메일 인증 코드");
        message.setText("인증 코드: " + authCode);

        // Step 2-2: 메일 전송
        try {
            emailSender.send(message);
        } catch (MailException e) {
            throw new AuthCodeException("이메일 발송에 실패했습니다.", e);
        }
    }

    // 인증코드 만료 시간 조회
    public Long getExpirationMillis() {
        return mailProperties.getAuthCodeExpirationMillis();
    }
}