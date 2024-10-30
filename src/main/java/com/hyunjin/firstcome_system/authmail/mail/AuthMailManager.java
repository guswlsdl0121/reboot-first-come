package com.hyunjin.firstcome_system.authmail.mail;

import com.hyunjin.firstcome_system.authmail.mail.config.MailProperties;
import com.hyunjin.firstcome_system.authmail.mail.exception.exception.AuthCodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class AuthMailManager {
    private final JavaMailSender emailSender;
    private final MailProperties mailProperties;

    public String generateAuthCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    public void sendCode(String to, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailProperties.getUsername());
        message.setTo(to);
        message.setSubject("이메일 인증 코드");
        message.setText("인증 코드: " + authCode);

        try {
            emailSender.send(message);
        } catch (MailException e) {
            throw new AuthCodeException("이메일 발송에 실패했습니다.", e);
        }
    }

    public Long getExpirationMillis() {
        return mailProperties.getAuthCodeExpirationMillis();
    }
}