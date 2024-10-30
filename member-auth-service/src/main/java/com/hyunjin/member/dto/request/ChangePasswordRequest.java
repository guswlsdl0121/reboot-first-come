package com.hyunjin.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ChangePasswordRequest(
        @NotBlank
        String currentPassword,

        @NotBlank
        String newPassword
) {
}