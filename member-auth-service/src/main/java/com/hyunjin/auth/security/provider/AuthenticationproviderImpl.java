package com.hyunjin.auth.security.provider;


import com.hyunjin.entity.Member;
import com.hyunjin.member.usecase.MemberFinder;
import com.hyunjin.member.usecase.MemberValidator;
import lombok.RequiredArgsConstructor;
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

@Component
@RequiredArgsConstructor
public class AuthenticationproviderImpl implements AuthenticationProvider {
    private final MemberFinder memberFinder;
    private final MemberValidator memberValidator;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        Member member = memberFinder.fetchByEmail(email);
        memberValidator.matchPassword(password, member.getPassword());

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(member.getRole().name())
        );

        UserDetails userDetails = User.builder()
                .username(member.getId().toString())
                .password(password)
                .authorities(authorities)
                .build();

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}