package com.jaelse.acc.lib.http_util;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class ResponseUtil {
    public static Mono<ServerResponse> unauthorized(String message) {
        return ServerResponse.status(HttpStatus.UNAUTHORIZED).bodyValue(message);
    }
}
