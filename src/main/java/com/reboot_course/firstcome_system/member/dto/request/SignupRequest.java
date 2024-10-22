package com.reboot_course.firstcome_system.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "회원가입 요청 DTO",
        example = """
               {
                 "name": "홍길동",
                 "password": "password123!",
                 "email": "hong@gmail.com",
                 "phone": "010-1234-5678",
                 "address": "서울시 강남구 테헤란로 123"
               }
               """)
public class SignupRequest {
    @NotBlank
    @Schema(description = "이름", example = "홍길동")
    private String name;

    @NotBlank
    @Schema(description = "비밀번호", example = "password123!")
    private String password;

    @Email
    @NotBlank
    @Schema(description = "이메일", example = "hong@gmail.com")
    private String email;

    @NotBlank
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phone;

    @NotBlank
    @Schema(description = "주소", example = "서울시 강남구 테헤란로 123")
    private String address;
}