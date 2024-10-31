package com.hyunjin.auth.encryption.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "encryption")
public class EncryptionProperties {
    private String key;
    private String salt;
}
