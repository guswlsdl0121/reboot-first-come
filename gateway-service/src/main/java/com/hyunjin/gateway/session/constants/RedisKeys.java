package com.hyunjin.gateway.session.constants;

public class RedisKeys {
    private static final String PREFIX = "spring:session:";

    // Session related keys
    public static final String SESSIONS = PREFIX + "data:";      // spring:session:data:{sessionId}

    private RedisKeys() {
    }

    public static String getSessionKey(String sessionId) {
        return SESSIONS + sessionId;
    }
}