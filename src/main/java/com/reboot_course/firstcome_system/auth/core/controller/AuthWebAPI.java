package com.reboot_course.firstcome_system.auth.core.controller;

import com.hyunjin.common.dto.CommonResponse;
import com.reboot_course.firstcome_system.auth.core.dto.request.LoginRequest;
import com.reboot_course.firstcome_system.auth.session.constants.SessionHeaders;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Authentication", description = "인증 관련 API")
public interface AuthWebAPI {

    @Operation(summary = "로그인", description = "사용자 인증을 수행하고 세션 토큰을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class)),
                    headers = @Header(name = SessionHeaders.X_AUTH_TOKEN,
                            description = "인증 세션 토큰",
                            schema = @Schema(type = "string")))
    })
    ResponseEntity<CommonResponse<String>> login(
            @Parameter(description = "로그인 요청 정보")
            @RequestBody LoginRequest loginRequest,
            @Parameter(hidden = true) HttpServletResponse response
    );

    @Operation(summary = "로그아웃", description = "현재 세션을 종료하고 로그아웃합니다.")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    ResponseEntity<Void> logout(
            @Parameter(description = "인증 세션 토큰", required = true)
            @RequestHeader(SessionHeaders.X_AUTH_TOKEN) String sessionId
    );
}