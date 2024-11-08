package com.hyunjin.security.provider;


import com.hyunjin.member.entity.Member;
import com.hyunjin.member.service.usecase.MemberFinder;
import com.hyunjin.member.service.usecase.MemberValidator;
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
        // Step 1: 인증 정보 추출
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Step 2: 회원 정보 조회 및 비밀번호 검증
        Member member = memberFinder.fetchByEmail(email);
        memberValidator.matchPassword(password, member.getPassword());

        // Step 3: 인증 토큰 생성
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(member.getRole().name())
        );

        UserDetails userDetails = User.builder()
                .username(member.getId().toString())
                .password(password)
                .authorities(authorities)
                .build();

        log.info("사용자 인증 성공. email: {}, role: {}", email, member.getRole());

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    // 지원하는 인증 타입 확인
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}