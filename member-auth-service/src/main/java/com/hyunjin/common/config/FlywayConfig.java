package com.hyunjin.common.config;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@RequiredArgsConstructor
public class FlywayConfig {
    private final Flyway flyway;

    @EventListener(ApplicationStartedEvent.class)
    public void migrateAndClean() {
        flyway.clean();     // 데이터베이스 클린
        flyway.migrate();   // 마이그레이션 실행
    }
}
