package com.jaelse.acc.api.accounts.handlers;

import com.jaelse.acc.api.accounts.models.AccountModel;
import com.jaelse.acc.lib.dtos.accounts.UpdateAccountDto;
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
public class UpdateAccountByIdHandler implements HandlerFunction<ServerResponse> {

    private final AccountsService service;

    public UpdateAccountByIdHandler(AccountsService service) {
        this.service = service;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.bodyToMono(UpdateAccountDto.class)
                .zipWith(Mono.just(HttpUtil.idFromPathVariable(request, "id")))
                .flatMap(cmdNId -> service.update(cmdNId.getT2(), cmdNId.getT1()))
                .flatMap(account -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(AccountModel.builder()
                                .id(account.getId())
                                .email(account.getEmail())
                                .name(account.getName())
                                .build())));
    }
}
