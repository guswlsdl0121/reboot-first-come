package com.hyunjin.gateway.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingFilter {
    private static final String CORRELATION_ID = "correlationId";
    private static final String REQUEST_PATH = "path";
    private static final String REQUEST_METHOD = "method";

    public Consumer<GatewayFilterSpec> apply() {
        return f -> f.filter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String correlationId = UUID.randomUUID().toString();

            // step 1: 요청 추적을 위한 MDC 설정
            MDC.put(CORRELATION_ID, correlationId);
            MDC.put(REQUEST_PATH, request.getPath().value());
            MDC.put(REQUEST_METHOD, request.getMethod().name());

            // step 2: 요청 헤더 로깅
            log.info("[Request Headers]\n{}", formatHeaders(request.getHeaders()));

            // step 3: 응답 처리 및 로깅
            return chain.filter(exchange)
                    .doFinally(signalType -> {
                        log.info("[Response Headers]\n{}",
                                formatHeaders(exchange.getResponse().getHeaders()));
                        MDC.clear();  // MDC 정리
                    });
        });
    }

    private String formatHeaders(HttpHeaders headers) {
        StringBuilder sb = new StringBuilder();
        headers.forEach((key, values) -> {
            String valueString = maskSensitiveData(key, String.join(", ", values));
            sb.append(String.format("  %s: %s\n", key, valueString));
        });
        return sb.toString();
    }

    private String maskSensitiveData(String headerName, String value) {
        List<String> sensitiveHeaders = Arrays.asList(
                "Authorization", "Cookie", "Set-Cookie",
                "X-Auth-Token", "X-User-Id");

        return sensitiveHeaders.contains(headerName) ? "********" : value;
    }
}