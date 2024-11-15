package com.hyunjin.wishlist.client.config;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        byte[] responseBody;
        try {
            responseBody = response.body() != null ?
                    response.body().asInputStream().readAllBytes() :
                    new byte[0];
        } catch (IOException e) {
            log.error("Error reading response body", e);
            responseBody = new byte[0];
        }

        return switch (response.status()) {
            case 404 -> new FeignException.NotFound(
                    "",  // blank message
                    response.request(),
                    responseBody,
                    response.headers()
            );
            case 400 -> new FeignException.BadRequest(
                    "",
                    response.request(),
                    responseBody,
                    response.headers()
            );
            default -> new FeignException.FeignServerException(
                    response.status(),
                    "",
                    response.request(),
                    responseBody,
                    response.headers()
            );
        };
    }
}