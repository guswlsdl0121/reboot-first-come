package com.hyunjin.gateway.constants.http;

public class HttpHeader {
    // Common Headers
    public static final String COOKIE = "Cookie";
    public static final String AUTHORIZATION = "Authorization";
    public static final String SET_COOKIE = "Set-Cookie";

    // Custom Headers
    public static final String AUTH_TOKEN = "X-Auth-Token";
    public static final String USER_ID = "X-User-Id";
    public static final String USER_ROLES = "X-User-Roles";

    private HttpHeader() {
    }
}
