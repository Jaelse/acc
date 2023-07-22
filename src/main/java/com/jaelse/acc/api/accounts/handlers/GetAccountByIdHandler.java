package com.jaelse.acc.api.accounts.handlers;

import com.jaelse.acc.api.accounts.models.AccountModel;
import com.jaelse.acc.lib.http_util.HttpUtil;
import com.jaelse.acc.resources.accounts.service.AccountsService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class GetAccountByIdHandler implements HandlerFunction<ServerResponse> {

    private final AccountsService service;

    public GetAccountByIdHandler(AccountsService service) {
        this.service = service;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return Mono.just(HttpUtil.idFromPathVariable(request, "id"))
                .flatMap(service::findById)
                .flatMap(account -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(AccountModel.builder()
                                .id(account.getId())
                                .name(account.getName())
                                .email(account.getEmail())
                                .build())
                        )
                );
    }
}
