package com.hyunjin.firstcome_system.auth.core.controller;

import com.hyunjin.firstcome_system.auth.core.dto.request.LoginRequest;
import com.hyunjin.firstcome_system.auth.core.service.AuthService;
import com.hyunjin.firstcome_system.auth.session.constants.SessionHeaders;
import com.hyunjin.firstcome_system.auth.utils.CommonResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthWebAPI {
    private final AuthService authService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<String>> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String sessionId = authService.login(loginRequest);
        response.setHeader(SessionHeaders.X_AUTH_TOKEN, sessionId);
        return ResponseEntity.ok(CommonResponse.success("로그인에 성공했습니다."));
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(SessionHeaders.X_AUTH_TOKEN) String sessionId) {
        authService.logout(sessionId);
        return ResponseEntity.ok().build();
    }
}