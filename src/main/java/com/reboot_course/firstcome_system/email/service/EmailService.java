package com.reboot_course.firstcome_system.email.service;

import com.reboot_course.firstcome_system.email.config.MailProperties;
import com.reboot_course.firstcome_system.email.repository.MailCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    private final MailCodeRepository mailCodeRepository;
    private final MailProperties mailProperties;

    public void sendCode(String email) {
        String authCode = String.format("%06d", new Random().nextInt(1000000));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailProperties.getUsername());
        message.setTo(email);
        message.setSubject("이메일 인증 코드");
        message.setText("인증 코드: " + authCode);

        mailCodeRepository.saveCode(email, authCode, mailProperties.getAuthCodeExpirationMillis());

        try {
            emailSender.send(message);
            log.info("이메일 인증 코드 발송 성공: {}", email);
        } catch (MailException e) {
            mailCodeRepository.removeCode(email);
            log.error("이메일 발송 실패: {}", e.getMessage());
            throw new RuntimeException("이메일 발송에 실패했습니다.", e);
        }
    }

    public void verifyCode(String email, String code) {
        String storedAuthCode = mailCodeRepository.getCode(email);
        if (storedAuthCode == null) {
            throw new RuntimeException("인증 코드가 만료되었거나 존재하지 않습니다.");
        }

        boolean isVerified = storedAuthCode.equals(code);
        if (isVerified) {
            mailCodeRepository.removeCode(email);
        }
    }
}