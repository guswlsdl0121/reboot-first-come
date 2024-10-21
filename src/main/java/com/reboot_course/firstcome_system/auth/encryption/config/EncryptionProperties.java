package com.reboot_course.firstcome_system.auth.encryption.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "encryption")
public class EncryptionProperties {
    private String key;
    private String salt;
}
