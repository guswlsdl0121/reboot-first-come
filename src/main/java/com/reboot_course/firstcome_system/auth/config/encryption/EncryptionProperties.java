package com.reboot_course.firstcome_system.auth.config.encryption;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@Component
@ConfigurationProperties(prefix = "encryption")
public class EncryptionProperties {
    private String key;
    private String salt;
}
