package com.jaelse.acc.api.accounts.handlers;

import com.jaelse.acc.api.accounts.models.AccountModel;
import com.jaelse.acc.lib.dtos.accounts.CreateAccountDto;
import com.jaelse.acc.lib.http_util.HttpUtil;
import com.jaelse.acc.resources.accounts.service.AccountsService;
import org.springframework.http.HttpStatus;
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

    public CreateAccountHandler(AccountsService service) {
        this.service = service;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        //Decode the body to give type.
        return request.bodyToMono(CreateAccountDto.class)
                //call the service create method passing the DTO
                .flatMap(service::create)
                //creating the Response
                .flatMap(account -> ServerResponse
                        .created(URI.create("/v1/accounts/" + account.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(AccountModel.builder()
                                .id(account.getId())
                                .name(account.getName())
                                .email(account.getEmail())
                                .build()))
                )
                // in case the account didn't get created because of some error
                .onErrorResume(HttpUtil::handleError)
                // in case the account didn't create for some reason
                .switchIfEmpty(ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .bodyValue("Unknown error"));
    }
}
