package com.hyunjin.mail.core.controller;

import com.hyunjin.mail.core.dto.request.EmailVerifyRequest;
import com.hyunjin.mail.core.service.EmailService;

import com.hyunjin.common.dto.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send-verification")
    public ResponseEntity<CommonResponse<String>> sendEmailVerification(@AuthenticationPrincipal UserDetails userDetails) {
        Integer memberId = Integer.parseInt(userDetails.getUsername());
        emailService.sendCode(memberId);
        return ResponseEntity.ok(CommonResponse.success("인증 코드가 이메일로 전송되었습니다."));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<CommonResponse<String>> verifyEmail(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody EmailVerifyRequest request) {
        Integer memberId = Integer.parseInt(userDetails.getUsername());
        emailService.verifyCode(memberId, request.getCode());
        return ResponseEntity.ok(CommonResponse.success("이메일 인증이 완료되었습니다."));
    }
}
