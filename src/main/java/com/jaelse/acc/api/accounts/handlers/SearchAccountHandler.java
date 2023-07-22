package com.jaelse.acc.api.accounts.handlers;

import com.jaelse.acc.api.accounts.models.AccountModel;
import com.jaelse.acc.api.accounts.models.SearchAccountsModel;
import com.jaelse.acc.lib.dtos.accounts.SearchAccountDto;
import com.jaelse.acc.resources.accounts.service.AccountsService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class SearchAccountHandler implements HandlerFunction<ServerResponse> {
    private final AccountsService service;

    public SearchAccountHandler(AccountsService service) {
        this.service = service;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.bodyToMono(SearchAccountDto.class)
                .flatMap(dto -> service.search(dto)
                        .map(account -> AccountModel.builder()
                                .id(account.getId())
                                .name(account.getName())
                                .email(account.getEmail())
                                .build())
                        .collectList())
                .flatMap(accounts -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(SearchAccountsModel.builder()
                                .accounts(accounts)
                                .build()))
                );

    }
}
