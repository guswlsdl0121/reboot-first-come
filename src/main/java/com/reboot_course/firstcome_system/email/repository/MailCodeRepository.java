package com.reboot_course.firstcome_system.email.repository;

public interface MailCodeRepository {
    void saveCode(String email, String authCode, long expirationTimeMillis);
    String getCode(String email);
    void removeCode(String email);
}