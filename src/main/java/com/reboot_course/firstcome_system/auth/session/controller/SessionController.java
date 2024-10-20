package com.reboot_course.firstcome_system.auth.session.controller;

import com.reboot_course.firstcome_system.auth.session.dto.request.LoginRequest;
import com.reboot_course.firstcome_system.auth.session.service.SessionService;
import com.reboot_course.firstcome_system.common.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/session")
public class SessionController {
    private static final String SESSION_HEADER = "X-Auth-Token";
    private final SessionService sessionService;

    @PostMapping("/login")
    public CommonResponse<String> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        CommonResponse<String> result = sessionService.login(loginRequest, session);
        if (result.isSuccess()) {
            response.setHeader(SESSION_HEADER, session.getId());
        }

        return result;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        sessionService.logout(request.getSession());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public CommonResponse<String> checkSession(@RequestHeader(SESSION_HEADER) String sessionId, HttpServletRequest request) {
        String userEmail = sessionService.getCurrentUser(sessionId);
        return CommonResponse.success(userEmail);
    }
}