package com.reboot_course.firstcome_system.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;
}