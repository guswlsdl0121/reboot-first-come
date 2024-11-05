package com.hyunjin.mail.core.repository;

import com.hyunjin.mail.mail.exception.exception.CodeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisMailCodeRepository implements MailCodeRepository {
    private static final String CODE_PREFIX = "mail:auth:code:";
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveCode(String email, String authCode, long expirationTimeMillis) {
        String key = getKey(email);
        redisTemplate.opsForValue().set(
                key,
                authCode,
                Duration.ofMillis(expirationTimeMillis)
        );
    }

    @Override
    public String getCode(String email) {
        String key = getKey(email);
        String code = (String) redisTemplate.opsForValue().get(key);

        if (code == null) {
            throw new CodeNotFoundException();
        }

        return code;
    }

    @Override
    public void removeCode(String email) {
        String key = getKey(email);
        redisTemplate.delete(key);
    }

    @Override
    public void verifyCode(String email, String code) {
        String storedCode = getCode(email);
        if (!storedCode.equals(code)) {
            throw new CodeNotFoundException();
        }
        removeCode(email);
    }

    private String getKey(String email) {
        return CODE_PREFIX + email;
    }
}