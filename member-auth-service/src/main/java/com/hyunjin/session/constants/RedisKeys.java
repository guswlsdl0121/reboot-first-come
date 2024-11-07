package com.hyunjin.session.constants;

import static com.hyunjin.session.constants.RedisFields.PRINCIPAL_NAME;

public class RedisKeys {
    private static final String PREFIX = "spring:session:";

    // Session related keys
    public static final String SESSIONS = PREFIX + "data:";      // spring:session:data:{sessionId}
    public static final String INDICES = PREFIX + "index:";      // spring:session:index:{indexName}

    private RedisKeys() {
    }

    public static String getSessionKey(String sessionId) {
        return SESSIONS + sessionId;
    }

    public static String getPrincipalIndexKey(String principalId) {
        return INDICES + PRINCIPAL_NAME + ":" + principalId;
    }
}