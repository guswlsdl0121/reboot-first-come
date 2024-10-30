package com.hyunjin.mail.mail.exception;


import com.hyunjin.mail.mail.exception.exception.AuthCodeException;
import com.hyunjin.mail.mail.exception.exception.CodeExpiredException;
import com.hyunjin.mail.mail.exception.exception.CodeNotFoundException;
import com.hyunjin.common.dto.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AuthMailExceptionHandler {

    @ExceptionHandler(CodeExpiredException.class)
    public ResponseEntity<CommonResponse<Void>> handleCodeExpiredException(CodeExpiredException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail(ex.getMessage()));
    }

    @ExceptionHandler(CodeNotFoundException.class)
    public ResponseEntity<CommonResponse<Void>> handleCodeNotFoundException(CodeNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail(ex.getMessage()));
    }

    @ExceptionHandler(AuthCodeException.class)
    public ResponseEntity<CommonResponse<Void>> handleAuthCodeException(AuthCodeException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponse.fail(ex.getMessage()));
    }
}
