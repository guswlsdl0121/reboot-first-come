package com.reboot_course.firstcome_system.common;

import com.reboot_course.firstcome_system.auth.encryption.config.EncryptionProperties;
import com.reboot_course.firstcome_system.auth.encryption.converter.EmailConverter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class CommonTestConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TextEncryptor twoWayAESEncryptor() {
        String salt = "0123456789abcdef0123456789abcdef";
        return Encryptors.text("testKey123456789", salt);
    }

    @Bean
    public EncryptionProperties encryptionProperties() {
        EncryptionProperties properties = new EncryptionProperties();
        properties.setKey("testEncryptionKey");
        return properties;
    }

    @Bean
    public EmailConverter emailConverter(EncryptionProperties encryptionProperties) {
        return new EmailConverter(encryptionProperties);
    }
}