package com.hyunjin.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Global", description = "글로벌 API")
public interface GlobalWebAPI {

    @Operation(summary = "Health Check", description = "서버의 상태를 확인합니다.")
    @ApiResponse(responseCode = "200", description = "서버가 정상적으로 동작중임",
            content = @Content(schema = @Schema(type = "string", example = "health check good")))
    ResponseEntity<String> healthCheck();
}
