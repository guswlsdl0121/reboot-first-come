package com.hyunjin.auth.encryption.config;

import io.micrometer.common.util.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * 기타 개인 정보 저장을 위한 양방향 암호화
     */
    @Bean
    @Primary
    public TextEncryptor twoWayAESEncryptor() {
        if (StringUtils.isEmpty(encryptionProperties.getKey()) ||
                StringUtils.isEmpty(encryptionProperties.getSalt())) {
            throw new IllegalStateException("Encryption key and salt must be configured");
        }
        return Encryptors.text(encryptionProperties.getKey(), encryptionProperties.getSalt());
    }
}