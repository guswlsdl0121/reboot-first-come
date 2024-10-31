package com.hyunjin.mail.core.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerifyRequest {
    @NotNull
    @Size(min = 6, max = 6)
    private String code;
}
