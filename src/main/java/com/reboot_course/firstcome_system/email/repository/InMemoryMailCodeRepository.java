package com.reboot_course.firstcome_system.email.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Primary
@Component
public class InMemoryMailCodeRepository implements MailCodeRepository {
    private final Map<String, AuthCode> store = new ConcurrentHashMap<>();

    @Scheduled(fixedRate = 60000) // 1분마다 만료된 코드 정리
    public void removeExpiredCodes() {
        long now = System.currentTimeMillis();
        store.entrySet().removeIf(entry -> entry.getValue().isExpired(now));
    }

    @Override
    public void saveCode(String email, String authCode, long expirationTimeMillis) {
        store.put(email, new AuthCode(authCode, System.currentTimeMillis() + expirationTimeMillis));
        log.debug("Auth code saved for email: {}", email);
    }

    @Override
    public String getCode(String email) {
        AuthCode authCode = store.get(email);
        if (authCode == null) {
            return null;
        }

        if (authCode.isExpired(System.currentTimeMillis())) {
            store.remove(email);
            return null;
        }

        return authCode.code();
    }

    @Override
    public void removeCode(String email) {
        store.remove(email);
        log.debug("Auth code removed for email: {}", email);
    }

    private record AuthCode(String code, long expirationTime) {
        public boolean isExpired(long currentTime) {
            return currentTime > expirationTime;
        }
    }
}