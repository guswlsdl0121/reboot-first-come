package com.reboot_course.firstcome_system.auth.security;

import com.reboot_course.firstcome_system.auth.security.exception.AuthenticationFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityManager {
    private final AuthenticationManager authenticationManager;

    public SecurityContext authenticate(String email, String password) {
        try {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            return context;
        } catch (Exception e) {
            throw new AuthenticationFailedException(e.getMessage());
        }
    }

    public void clearContext() {
        SecurityContextHolder.clearContext();
    }
}