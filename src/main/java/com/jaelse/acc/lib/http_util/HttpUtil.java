package com.jaelse.acc.lib.http_util;

import com.jaelse.acc.lib.exceptions.InvalidIdException;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Optional;

public class HttpUtil {

    public static Integer idFromPathVariable(ServerRequest request, String name) {
        return Optional.ofNullable(request)
                .map(r -> r.pathVariable(name))
                .map(Integer::parseInt)
                .orElseThrow(() -> new InvalidIdException("Id provided in the path varialbe id not valid"));
    }
}
