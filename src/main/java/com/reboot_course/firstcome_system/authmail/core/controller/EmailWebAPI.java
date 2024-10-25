package com.reboot_course.firstcome_system.authmail.core.controller;

import com.reboot_course.firstcome_system.common.dto.CommonResponse;
import com.reboot_course.firstcome_system.authmail.core.dto.request.EmailVerifyRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "이메일 인증", description = "이메일 인증 API")
public interface EmailWebAPI {
    @Operation(summary = "이메일 코드로 인증", description = "이메일 인증 코드를 검증합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })
    ResponseEntity<CommonResponse<String>> verifyEmail(
            @Parameter(description = "사용자 정보")
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "이메일 인증 요청 정보")
            @Valid @RequestBody EmailVerifyRequest request
    );

    @Operation(summary = "이메일 인증 코드 전송", description = "입력한 이메일로 인증 코드를 전송합니다.")
    @ApiResponse(responseCode = "200", description = "인증 코드 전송 성공")
    ResponseEntity<CommonResponse<String>> sendEmailVerification(
            @Parameter(description = "이메일 주소", required = true)
            @AuthenticationPrincipal UserDetails userDetails
    );
}
