package com.hyunjin.auth.api.controller;


import com.hyunjin.auth.api.dto.request.LoginRequest;
import com.hyunjin.auth.service.AuthService;
import com.hyunjin.common.dto.CommonResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<String>> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String sessionId = authService.login(loginRequest);

        // 쿠키 생성 및 설정
        ResponseCookie cookie = ResponseCookie.from("SESSION", sessionId)
                .httpOnly(true)                 // JS에서 접근 불가
                .secure(true)                   // HTTPS에서만 전송
                .sameSite("Strict")             // CSRF 방지
                .path("/")                      // 모든 경로에서 사용 가능
                .maxAge(Duration.ofHours(1))    // 1시간 유효
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(CommonResponse.success("로그인에 성공했습니다."));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue("SESSION") String sessionId, HttpServletResponse response) {
        authService.logout(sessionId);

        // 쿠키 삭제
        ResponseCookie cookie = ResponseCookie.from("SESSION", "")
                .maxAge(0)
                .path("/")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().build();
    }
}