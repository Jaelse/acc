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
        // Decoding Body to DTO class
        return request.bodyToMono(SearchAccountDto.class)
                //Calling search to find the accounts matching the string
                .flatMap(dto -> service.search(dto)
                        //Building the account model
                        .map(account -> AccountModel.builder()
                                .id(account.getId())
                                .name(account.getName())
                                .email(account.getEmail())
                                .build())
                        //collecting back to list
                        .collectList())
                //making the response
                .flatMap(accounts -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(SearchAccountsModel.builder()
                                .accounts(accounts)
                                .build()))
                )
                //sending not found if nothing could be found
                .switchIfEmpty(ServerResponse.notFound().build());

    }
}
