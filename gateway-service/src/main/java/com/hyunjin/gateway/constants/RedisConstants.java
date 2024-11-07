package com.hyunjin.gateway.constants;

public class RedisConstants {
    public static final String SESSION_PREFIX = "spring:session:sessions:";
    public static final String SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
    public static final String LAST_ACCESSED_TIME = "lastAccessedTime";

    private RedisConstants() {
        throw new IllegalStateException("상수 클래스는 인스턴스화할 수 없습니다");
    }
}