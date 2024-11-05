package com.hyunjin.auth.security;

import com.hyunjin.auth.security.exception.AuthenticationFailedException;
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
        log.debug("Starting authentication process for email: {}", email);

        try {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            log.debug("Created empty security context");

            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(email, password);
            log.debug("Created authentication token: {}", authRequest);

            Authentication authentication;
            try {
                authentication = authenticationManager.authenticate(authRequest);
                log.debug("AuthenticationManager successfully authenticated user. Authorities: {}",
                        authentication.getAuthorities());
            } catch (Exception authEx) {
                log.error("Authentication failed in AuthenticationManager", authEx);
                if (authEx.getCause() != null) {
                    log.error("Caused by: ", authEx.getCause());
                }
                throw authEx;
            }

            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            log.debug("Successfully set security context with authenticated user");

            return context;
        } catch (Exception e) {
            log.error("Authentication failed for email: {}", email);
            log.error("Error type: {}", e.getClass().getName());
            log.error("Error message: {}", e.getMessage());
            if (e.getCause() != null) {
                log.error("Root cause: {} - {}",
                        e.getCause().getClass().getName(),
                        e.getCause().getMessage());
            }
            throw new AuthenticationFailedException("Authentication failed: " + e.getMessage());
        }
    }

    public void clearContext() {
        log.debug("Clearing security context");
        SecurityContextHolder.clearContext();
    }
}