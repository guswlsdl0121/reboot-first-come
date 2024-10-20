package com.reboot_course.firstcome_system.auth.session.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableSpringHttpSession
public class SessionConfig {

    @Bean
    public MapSessionRepository sessionRepository() {
        return new MapSessionRepository(new ConcurrentHashMap<>());
    }

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return new HeaderHttpSessionIdResolver("X-Auth-Token");
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID"); // 세션 쿠키 이름 설정
        serializer.setCookiePath("/"); // 모든 경로에서 쿠키 사용 가능
        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$"); // 서브도메인 포함
        return serializer;
    }
}