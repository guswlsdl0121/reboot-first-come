package com.hyunjin.security;

import com.hyunjin.wishlist.client.config.ErrorResponse;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException e) {
        log.error("Handling FeignException: ", e);
        return ResponseEntity
                .status(e.status())
                .body(ErrorResponse.of(
                        e.contentUTF8(),  // 원본 에러 메시지 사용
                        "FEIGN_ERROR"
                ));
    }
}
