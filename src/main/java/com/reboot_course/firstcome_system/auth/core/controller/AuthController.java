package com.reboot_course.firstcome_system.auth.core.controller;

import com.reboot_course.firstcome_system.auth.core.dto.request.LoginRequest;
import com.reboot_course.firstcome_system.auth.core.service.AuthService;
import com.reboot_course.firstcome_system.common.dto.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<String>> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        String sessionId = authService.login(loginRequest, request.getSession(true));
        response.setHeader("X-Auth-Token", sessionId);
        return ResponseEntity.ok(CommonResponse.success("로그인에 성공했습니다."));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authService.logout(request.getSession(false));
        return ResponseEntity.ok().build();
    }
}