package com.hyunjin.auth.session.config;


import com.hyunjin.auth.session.repository.CustomSessionRepository;
import com.hyunjin.auth.session.repository.InmemoryIndexSessionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSession;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableSpringHttpSession
public class SessionConfig {

    @Bean
    public Map<String, MapSession> sessionMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public CustomSessionRepository sessionRepository(Map<String, MapSession> sessionMap) {
        return new InmemoryIndexSessionRepository(sessionMap);
    }

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return new HeaderHttpSessionIdResolver("X-Auth-Token");
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID");
        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
        serializer.setUseSecureCookie(true);
        serializer.setSameSite("Strict");
        return serializer;
    }
}