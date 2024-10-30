package com.hyunjin.firstcome_system.order.utils;

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

    /**
     * 성공 응답을 생성합니다.
     *
     * @param message 응답 메시지
     * @param data    응답 데이터
     * @param <T>     응답 데이터의 타입
     * @return 생성된 CommonResponse 객체
     */
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