package com.reboot_course.firstcome_system.member.dto.request;

import lombok.Builder;

@Builder
public record ChangePasswordRequest(String currentPassword, String newPassword) {
}
