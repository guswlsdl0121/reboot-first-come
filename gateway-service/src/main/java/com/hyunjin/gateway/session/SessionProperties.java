package com.hyunjin.gateway.session;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.session")
public class SessionProperties {
    private Cookie cookie;
    private Integer timeout;

    @Getter
    @Setter
    public static class Cookie {
        private String name;
    }
}