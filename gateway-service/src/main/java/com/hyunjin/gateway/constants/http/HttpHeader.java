package com.hyunjin.gateway.constants.http;

public class HttpHeader {
    // Common Headers
    public static final String COOKIE = "Cookie";

    // Custom Headers
    public static final String AUTH_TOKEN = "X-Auth-Token";
    public static final String USER_ID = "X-User-Id";
    public static final String USER_ROLES = "X-User-Roles";

    private HttpHeader() {
    }
}
