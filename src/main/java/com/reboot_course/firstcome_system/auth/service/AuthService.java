package com.reboot_course.firstcome_system.auth.service;

import com.reboot_course.firstcome_system.auth.controller.SignupRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    public int signUp(@Valid SignupRequest request) {
        return 0;
    }
}
