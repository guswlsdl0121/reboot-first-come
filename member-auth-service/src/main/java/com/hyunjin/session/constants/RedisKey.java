package com.hyunjin.session.constants;

public class RedisKey {
    private static final String BASE = "spring:session:";
    private static final String SESSIONS = BASE + "sessions:";
    public static final String SESSION = SESSIONS + "session:";
    private static final String INDEX = BASE + "index:";
    public static final String PRINCIPAL_NAME_INDEX = INDEX + "principal_name:";

    private RedisKey() {
    }
}