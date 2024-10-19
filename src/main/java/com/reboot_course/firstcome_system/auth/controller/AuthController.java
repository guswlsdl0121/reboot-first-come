package com.reboot_course.firstcome_system.auth.controller;

import com.reboot_course.firstcome_system.auth.dto.request.SignupRequest;
import com.reboot_course.firstcome_system.auth.service.AuthService;
import com.reboot_course.firstcome_system.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}