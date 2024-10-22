package com.reboot_course.firstcome_system.auth.security.config;

import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Setter(AccessLevel.PACKAGE)
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    private List<String> publicUrls;
    public String[] getPublicUrls() {
        return publicUrls.toArray(new String[0]);
    }
}