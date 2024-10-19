package com.reboot_course.firstcome_system.auth.config.encryption;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class EncryptionConfig {

    private final EncryptionProperties encryptionProperties;

    public EncryptionConfig(EncryptionProperties encryptionProperties) {
        this.encryptionProperties = encryptionProperties;
    }

    /*
     * 비밀번호 저장을 위한 단방향 해시
     */
    @Bean
    public PasswordEncoder oneWayHashEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * 기타 개인 정보 저장을 위한 양방향 암호화
     */
    @Bean
    public TextEncryptor twoWayAESEncryptor() {
        return Encryptors.text(encryptionProperties.getKey(), encryptionProperties.getSalt());
    }
}