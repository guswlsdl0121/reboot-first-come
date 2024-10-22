package com.reboot_course.firstcome_system.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "비밀번호 변경 요청 DTO",
        example = """
                {
                  "currentPassword": "password123!",
                  "newPassword": "changepassword123!"
                }
                """)
public record ChangePasswordRequest(
        @NotBlank
        @Schema(description = "현재 비밀번호", example = "password123!")
        String currentPassword,

        @NotBlank
        @Schema(description = "새로운 비밀번호", example = "changepassword123!")
        String newPassword
) {
}