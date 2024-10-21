package com.reboot_course.firstcome_system.common.exception.handler;

import com.reboot_course.firstcome_system.common.dto.CommonResponse;
import com.reboot_course.firstcome_system.common.exception.exception.DuplicatedException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // 400: 잘못된 요청
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        CommonResponse<Void> response = CommonResponse.fail(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //404 : 무언가를 찾을 수 없을 때 (Entitiy)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CommonResponse<Void>> handleEntityNotFoundException(EntityNotFoundException ex) {
        CommonResponse<Void> response = CommonResponse.fail(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    //409 : 중복된 값이나 요청
    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<CommonResponse<Void>> handleDuplicatedException(DuplicatedException ex) {
        CommonResponse<Void> response = CommonResponse.fail(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    //500 : 그외에 잡히지 않는 서버 에러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleAllUncaughtException(Exception ex) {
        CommonResponse<Void> response = CommonResponse.fail(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
