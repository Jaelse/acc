package com.jaelse.acc.api.accounts.handlers;

import com.jaelse.acc.api.accounts.models.AccountModel;
import com.jaelse.acc.lib.Role;
import com.jaelse.acc.lib.dtos.accounts.UpdateAccountDto;
import com.jaelse.acc.lib.http_util.HttpUtil;
import com.jaelse.acc.lib.http_util.ResponseUtil;
import com.jaelse.acc.resources.accounts.service.AccountsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
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
        // decode the body to the DTO class
        return request.bodyToMono(UpdateAccountDto.class)
                // zip the dto with id from the path variable
                .zipWith(Mono.just(HttpUtil.idFromPathVariable(request, "id")))
                //Get the current security context
                .flatMap(cmdNId -> ReactiveSecurityContextHolder.getContext()
                        .map(SecurityContext::getAuthentication)
                        // Check if the user requesting for their own resource or if the user is an Admin
                        .filter(authentication -> cmdNId.getT2().equals(authentication.getCredentials()) ||
                                authentication.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROLE_ADMIN.toString())))
                        .flatMap(ignored -> authorizedFlow(cmdNId.getT1(), cmdNId.getT2()))
                        // User is not authorized to update this resource
                        .switchIfEmpty(ResponseUtil.unauthorized("Unauthorized to access this resource")));
    }


    private Mono<ServerResponse> authorizedFlow(UpdateAccountDto dto, Integer id) {
        //Update the account
        return service.update(id, dto)
                //make the response
                .flatMap(account -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(AccountModel.builder()
                                .id(account.getId())
                                .email(account.getEmail())
                                .name(account.getName())
                                .build())))
                // in case the account could not be updated
                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .bodyValue("Unable to create the account. Please try again."))
                // in case the account user trying to update doesn't not exists
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
