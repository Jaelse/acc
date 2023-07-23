package com.jaelse.acc.api.authorization.handlers;

import com.jaelse.acc.api.authorization.models.AuthorizationModel;
import com.jaelse.acc.configurations.security.JwtUtil;
import com.jaelse.acc.configurations.security.PBKDF2Encoder;
import com.jaelse.acc.lib.dtos.authorization.LoginDto;
import com.jaelse.acc.resources.accounts.service.AccountsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@Component
public class LoginHandler implements HandlerFunction<ServerResponse> {

    private final AccountsService accountsService;

    public LoginHandler(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.bodyToMono(LoginDto.class)
                .flatMap(accountsService::authenticate)
                .flatMap(token -> ServerResponse.ok()
                        .body(BodyInserters.fromValue(AuthorizationModel.builder()
                                .token(token)
                                .build())
                        )
                )
                .switchIfEmpty(ServerResponse
                        .status(HttpStatus.UNAUTHORIZED)
                        .bodyValue("Not Authenticated"));
    }
}
