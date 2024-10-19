package com.reboot_course.firstcome_system.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class EncryptionConfig {

    /*
     * 비밀번호 저장을 위한 단방향 해시
     */
    @Bean
    public PasswordEncoder oneWayHashEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * 기타 개인 정보 저장을 위한 양방향 암호화
     * 중요!! 실제 개발에선 이 복호화 값과 솔트값을 환경변수나 안전한 곳에서 사용해야 함.
     * 현재는 개발 과정이기 때문에, 넘어간다.
     */
    @Bean
    public TextEncryptor twoWayAESEncryptor() {
        String encryptionKey = "0123456789abcdef"; // 16, 24, 또는 32 바이트
        String salt = "0123456789abcdef"; // 16 바이트
        return Encryptors.text(encryptionKey, salt);
    }
}