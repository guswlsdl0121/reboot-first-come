package com.reboot_course.firstcome_system.authmail.core.repository;

import com.reboot_course.firstcome_system.authmail.mail.exception.exception.CodeExpiredException;
import com.reboot_course.firstcome_system.authmail.mail.exception.exception.CodeNotFoundException;
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

    @Scheduled(fixedRate = 60000)
    public void removeExpiredCodes() {
        long now = System.currentTimeMillis();
        store.entrySet().removeIf(entry -> entry.getValue().isExpired(now));
    }

    @Override
    public void saveCode(String email, String authCode, long expirationTimeMillis) {
        store.put(email, new AuthCode(authCode, System.currentTimeMillis() + expirationTimeMillis));
    }

    @Override
    public String getCode(String email) {
        AuthCode authCode = store.get(email);
        if (authCode == null) {
            throw new CodeNotFoundException();
        }

        if (authCode.isExpired(System.currentTimeMillis())) {
            store.remove(email);
            throw new CodeExpiredException();
        }

        return authCode.code();
    }

    @Override
    public void removeCode(String email) {
        store.remove(email);
    }

    @Override
    public void verifyCode(String email, String code) {
        String storedCode = getCode(email);
        if (!storedCode.equals(code)) {
            throw new CodeNotFoundException();
        }
        removeCode(email);
    }

    private record AuthCode(String code, long expirationTime) {
        public boolean isExpired(long currentTime) {
            return currentTime > expirationTime;
        }
    }
}