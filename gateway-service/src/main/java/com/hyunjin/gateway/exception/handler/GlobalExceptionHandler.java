package com.hyunjin.gateway.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyunjin.gateway.exception.dto.ErrorResponse;
import com.hyunjin.gateway.exception.exception.AuthenticationException;
import com.hyunjin.gateway.exception.exception.SessionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@Order(-2)  // WebFlux의 DefaultErrorWebExceptionHandler보다 먼저 실행
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        if (ex instanceof SessionException) {
            log.error("세션 오류: {}", ex.getMessage());
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return writeErrorResponse(response, "401", ex.getMessage());
        }

        if (ex instanceof AuthenticationException) {
            log.error("인증 오류: {}", ex.getMessage());
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return writeErrorResponse(response, "403", ex.getMessage());
        }

        log.error("예상치 못한 오류", ex);
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return writeErrorResponse(response, "500", "서버 오류가 발생했습니다.");
    }

    private Mono<Void> writeErrorResponse(ServerHttpResponse response, String code, String message) {
        ErrorResponse errorResponse = new ErrorResponse(code, message);
        byte[] bytes;
        try {
            bytes = new ObjectMapper().writeValueAsBytes(errorResponse);
        } catch (JsonProcessingException e) {
            log.error("Error response 생성 중 오류", e);
            bytes = "{}".getBytes(StandardCharsets.UTF_8);
        }

        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}