package com.reboot_course.firstcome_system.auth.session.service;

import com.reboot_course.firstcome_system.auth.session.dto.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    public String login(LoginRequest loginRequest, HttpServletRequest request) {
        return null;
    }

    public void logout(HttpServletRequest request) {
    }
}