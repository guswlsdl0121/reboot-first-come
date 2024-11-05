package com.hyunjin.auth.security.provider;


import com.hyunjin.entity.Member;
import com.hyunjin.member.usecase.MemberFinder;
import com.hyunjin.member.usecase.MemberValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationproviderImpl implements AuthenticationProvider {
    private final MemberFinder memberFinder;
    private final MemberValidator memberValidator;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            String email = authentication.getName();
            String password = authentication.getCredentials().toString();

            log.debug("Attempting to authenticate user: {}", email);

            Member member = memberFinder.fetchByEmail(email);
            log.debug("Found member with ID: {}", member.getId());

            memberValidator.matchPassword(password, member.getPassword());
            log.debug("Password validation successful");

            List<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority(member.getRole().name())
            );

            UserDetails userDetails = User.builder()
                    .username(member.getId().toString())
                    .password(password)
                    .authorities(authorities)
                    .build();

            log.debug("Created UserDetails with authorities: {}", authorities);

            return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        } catch (Exception e) {
            log.error("Authentication provider failed", e);
            throw e;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}