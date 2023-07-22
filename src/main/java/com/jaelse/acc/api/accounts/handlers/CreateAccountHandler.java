package com.jaelse.acc.api.accounts.handlers;

import com.jaelse.acc.api.accounts.models.AccountModel;
import com.jaelse.acc.lib.dtos.accounts.CreateAccountDto;
import com.jaelse.acc.resources.accounts.service.AccountsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class CreateAccountHandler implements HandlerFunction<ServerResponse> {

    private final AccountsService service;
    private final Logger logger = LoggerFactory.getLogger(CreateAccountHandler.class);

    public CreateAccountHandler(AccountsService service) {
        this.service = service;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.bodyToMono(CreateAccountDto.class)
                .flatMap(service::create)
                .flatMap(account -> ServerResponse
                        .created(URI.create("/v1/accounts/" + account.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(AccountModel.builder()
                                .id(account.getId())
                                .name(account.getName())
                                .email(account.getEmail())
                                .build()))
                );
    }
}
