package com.hyunjin.firstcome_system.authmail.core.repository;

public interface MailCodeRepository {
    void saveCode(String email, String authCode, long expirationTimeMillis);

    String getCode(String email);

    void removeCode(String email);

    void verifyCode(String email, String code);
}