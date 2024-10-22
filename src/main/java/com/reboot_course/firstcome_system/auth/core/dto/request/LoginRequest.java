package com.reboot_course.firstcome_system.auth.core.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "로그인 요청 DTO",
        example = """
               {
                 "email": "hong@gmail.com",
                 "password": "password123!"
               }
               """)
public class LoginRequest {
    @Schema(description = "이메일", example = "hong@gmail.com")
    private String email;

    @Schema(description = "비밀번호", example = "password123!")
    private String password;
}