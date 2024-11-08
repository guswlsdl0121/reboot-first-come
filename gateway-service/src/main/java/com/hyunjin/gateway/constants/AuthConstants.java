package com.hyunjin.gateway.constants;

import java.util.List;

public class AuthConstants {
    // Headers
    public static final String X_AUTH_TOKEN = "X-Auth-Token";
    public static final String X_USER_ID = "X-User-Id";
    public static final String X_USER_ROLES = "X-User-Roles";

    // Public API Paths
    public static final List<String> PUBLIC_PATHS = List.of(
            "/api/v1/auth/login",
            "/api/v1/member/signup",
            "/api/v1/product"
    );

    private AuthConstants() {
    }
}
