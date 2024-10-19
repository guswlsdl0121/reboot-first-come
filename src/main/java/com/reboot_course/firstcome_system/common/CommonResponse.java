package com.reboot_course.firstcome_system.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    private boolean success;
    private String message;
    private T data;

    // 성공 응답 생성
    public static <T> CommonResponse<T> success(String message, T data) {
        return new CommonResponse<>(true, message, data);
    }

    // 성공 응답 생성 (데이터만)
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(true, "Success", data);
    }

    // 실패 응답 생성
    public static <T> CommonResponse<T> fail(String message) {
        return new CommonResponse<>(false, message, null);
    }
}