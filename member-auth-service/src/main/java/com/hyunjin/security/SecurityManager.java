package com.hyunjin.security;

import com.hyunjin.security.exception.AuthenticationFailedException;
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
            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(email, password);

            Authentication authentication = authenticationManager.authenticate(authRequest);
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            log.debug("Authentication process completed for user: {}", email);

            return context;
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", email, e);
            throw new AuthenticationFailedException("Authentication failed: " + e.getMessage());
        }
    }

    public void clearContext() {
        SecurityContextHolder.clearContext();
    }
}