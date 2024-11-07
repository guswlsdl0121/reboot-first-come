package com.hyunjin.gateway.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyunjin.gateway.exception.dto.ErrorResponse;
import com.hyunjin.gateway.exception.exception.GatewayException;
import com.hyunjin.gateway.exception.exception.SessionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(-2)
@RequiredArgsConstructor
public class GlobalExceptionHandler implements WebExceptionHandler {
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        if (ex instanceof SessionException) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return writeErrorResponse(response, "세션 정보가 잘못됐습니다.");
        }

        if (ex instanceof GatewayException) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return writeErrorResponse(response, ex.getMessage());
        }

        log.error("처리되지 않은 예외 발생", ex);
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return writeErrorResponse(response, "서버 내부 오류가 발생했습니다");
    }

    private Mono<Void> writeErrorResponse(ServerHttpResponse response, String message) {
        try {
            ErrorResponse errorResponse = new ErrorResponse(false, message);
            byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("에러 응답 생성 중 실패", e);
            return Mono.error(new GatewayException("응답 생성 실패", e));
        }
    }
}
