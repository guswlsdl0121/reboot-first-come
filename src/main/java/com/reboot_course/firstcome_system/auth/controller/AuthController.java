package com.reboot_course.firstcome_system.auth.controller;

import com.reboot_course.firstcome_system.auth.dto.request.LoginRequest;
import com.reboot_course.firstcome_system.auth.dto.request.SignupRequest;
import com.reboot_course.firstcome_system.auth.service.AuthService;
import com.reboot_course.firstcome_system.common.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<Integer>> signup(@RequestBody @Valid SignupRequest request) {
        int memberId = authService.signUp(request);
        return ResponseEntity.ok(CommonResponse.success("User registered successfully", memberId));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        String sessionId = authService.login(loginRequest, request);
        return ResponseEntity.ok(sessionId);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<CommonResponse<String>> getCurrentUser() {
        String email = authService.getCurrentUserEmail();
        return ResponseEntity.ok(CommonResponse.success("Current user retrieved successfully", email));
    }
}