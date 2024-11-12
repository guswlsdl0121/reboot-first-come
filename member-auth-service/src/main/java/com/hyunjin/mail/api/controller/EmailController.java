package com.hyunjin.mail.api.controller;

import com.hyunjin.common.dto.CommonResponse;
import com.hyunjin.mail.api.dto.request.EmailVerifyRequest;
import com.hyunjin.mail.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send-verification")
    public ResponseEntity<CommonResponse<String>> sendEmailVerification(@AuthenticationPrincipal UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer memberId = Integer.parseInt(auth.getName());

        emailService.sendCode(memberId);

        return ResponseEntity.ok(CommonResponse.success("인증 코드가 이메일로 전송되었습니다."));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<CommonResponse<String>> verifyEmail(@Valid @RequestBody EmailVerifyRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer memberId = Integer.parseInt(auth.getName());

        emailService.verifyCode(memberId, request.getCode());
        return ResponseEntity.ok(CommonResponse.success("이메일 인증이 완료되었습니다."));
    }
}
