package com.reboot_course.firstcome_system.email.controller;

import com.reboot_course.firstcome_system.common.dto.CommonResponse;
import com.reboot_course.firstcome_system.email.dto.request.EmailVerifyRequest;
import com.reboot_course.firstcome_system.email.service.EmailService;
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
public class EmailController implements EmailWebAPI {
    private final EmailService emailService;

    @Override
    @PostMapping("/send-verification")
    public ResponseEntity<CommonResponse<String>> sendEmailVerification(@AuthenticationPrincipal UserDetails userDetails) {
        emailService.sendCode(userDetails.getUsername());
        return ResponseEntity.ok(CommonResponse.success("인증 코드가 이메일로 전송되었습니다."));
    }

    @Override
    @PostMapping("/verify-email")
    public ResponseEntity<CommonResponse<String>> verifyEmail(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody EmailVerifyRequest request) {
        emailService.verifyCode(userDetails.getUsername(), request.getCode());
        return ResponseEntity.ok(CommonResponse.success("이메일 인증이 완료되었습니다."));
    }
}
