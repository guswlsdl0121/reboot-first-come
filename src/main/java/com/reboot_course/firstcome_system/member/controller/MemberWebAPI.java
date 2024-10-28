package com.reboot_course.firstcome_system.member.controller;

import com.reboot_course.firstcome_system.member.dto.request.ChangePasswordRequest;
import com.reboot_course.firstcome_system.member.dto.request.SignupRequest;
import com.reboot_course.firstcome_system.member.utils.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@Tag(name = "Member", description = "회원 관련 API")
public interface MemberWebAPI {

    @Operation(summary = "현재 로그인한 회원 정보 조회", description = "현재 인증된 회원의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 회원 정보를 조회함",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    CommonResponse<String> showPrincipal(
            @Parameter(description = "인증된 사용자 정보", hidden = true)
            @AuthenticationPrincipal UserDetails userDetails
    );

    @Operation(summary = "회원가입", description = "새로운 회원을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 회원가입됨",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })
    ResponseEntity<CommonResponse<Integer>> signup(
            @Parameter(description = "회원가입 요청 정보")
            @RequestBody @Valid SignupRequest request
    );

    @Operation(summary = "비밀번호 변경", description = "로그인한 회원의 비밀번호를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 비밀번호를 변경함",
                    content = @Content(schema = @Schema(type = "string", example = "비밀번호가 성공적으로 변경됐습니다.")))
    })
    ResponseEntity<String> changePassword(
            @Parameter(description = "인증된 사용자 정보", hidden = true)
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "비밀번호 변경 요청 정보")
            @RequestBody ChangePasswordRequest request
    );
}
