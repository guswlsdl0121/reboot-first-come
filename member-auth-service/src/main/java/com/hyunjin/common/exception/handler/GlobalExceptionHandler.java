package com.hyunjin.common.exception.handler;

import com.hyunjin.common.dto.CommonResponse;
import com.hyunjin.common.exception.exception.DuplicatedException;
import com.hyunjin.mail.exception.AuthCodeException;
import com.hyunjin.mail.exception.CodeExpiredException;
import com.hyunjin.mail.exception.CodeNotFoundException;
import com.hyunjin.security.exception.AuthenticationFailedException;
import com.hyunjin.session.exception.SessionException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 BAD_REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.warn("잘못된 요청 파라미터", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail("잘못된 요청입니다."));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<CommonResponse<Void>> handleBindException(BindException e) {
        log.warn("잘못된 요청 파라미터", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail("잘못된 요청입니다."));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<CommonResponse<Void>> handleValidationException(ValidationException e) {
        log.warn("요청 데이터 검증 실패", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("잘못된 인자", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail(e.getMessage()));
    }

    // 401 UNAUTHORIZED
    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<CommonResponse<Void>> handleAuthenticationFailedException(AuthenticationFailedException e) {
        log.warn("인증 실패", e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponse.fail(e.getMessage()));
    }

    // 403 FORBIDDEN
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CommonResponse<Void>> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("접근 권한 없음", e);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(CommonResponse.fail("접근 권한이 없습니다."));
    }

    // 404 NOT_FOUND
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CommonResponse<Void>> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn("엔티티 조회 실패", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(CommonResponse.fail(e.getMessage()));
    }

    // 409 CONFLICT
    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<CommonResponse<Void>> handleDuplicatedException(DuplicatedException e) {
        log.warn("중복된 데이터", e);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(CommonResponse.fail(e.getMessage()));
    }

    // 이메일 인증 관련 예외
    @ExceptionHandler(CodeExpiredException.class)
    public ResponseEntity<CommonResponse<Void>> handleCodeExpiredException(CodeExpiredException e) {
        log.warn("인증 코드 만료", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail(e.getMessage()));
    }

    @ExceptionHandler(CodeNotFoundException.class)
    public ResponseEntity<CommonResponse<Void>> handleCodeNotFoundException(CodeNotFoundException e) {
        log.warn("인증 코드 없음", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail(e.getMessage()));
    }

    @ExceptionHandler(AuthCodeException.class)
    public ResponseEntity<CommonResponse<Void>> handleAuthCodeException(AuthCodeException e) {
        log.error("인증 코드 처리 실패", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponse.fail(e.getMessage()));
    }

    // 세션 관련 예외
    @ExceptionHandler(SessionException.class)
    public ResponseEntity<CommonResponse<Void>> handleSessionException(SessionException e) {
        log.error("세션 처리 실패", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponse.fail(e.getMessage()));
    }

    // 500 INTERNAL_SERVER_ERROR
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleException(Exception e) {
        log.error("예상치 못한 에러 발생", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponse.fail("서버 에러가 발생했습니다."));
    }
}