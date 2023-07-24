package com.jaelse.acc.lib.http_util;

import com.jaelse.acc.lib.exceptions.InvalidIdException;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class HttpUtil {

    public static Integer idFromPathVariable(ServerRequest request, String name) {
        return Optional.ofNullable(request)
                .map(r -> r.pathVariable(name))
                .map(Integer::parseInt)
                .orElseThrow(() -> new InvalidIdException("Id provided in the path varialbe id not valid"));
    }


    public static Mono<ServerResponse> handleError(Throwable throwable) {
        if (throwable.getClass().equals(org.springframework.dao.DuplicateKeyException.class)) {
            return ServerResponse.status(HttpStatus.CONFLICT)
                    .bodyValue("A user with same email exists");
        } else {
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .bodyValue("Unable to create the account. Please try again.");
        }
    }
}
