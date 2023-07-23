package com.jaelse.acc.api.accounts.handlers;

import com.jaelse.acc.api.accounts.models.AccountModel;
import com.jaelse.acc.configurations.security.SecurityContextRepository;
import com.jaelse.acc.lib.Role;
import com.jaelse.acc.lib.http_util.HttpUtil;
import com.jaelse.acc.lib.http_util.ResponseUtil;
import com.jaelse.acc.resources.accounts.service.AccountsService;
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
public class GetAccountByIdHandler implements HandlerFunction<ServerResponse> {

    private final AccountsService service;

    public GetAccountByIdHandler(AccountsService service) {
        this.service = service;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        //Get the id from the path variable
        return Mono.just(HttpUtil.idFromPathVariable(request, "id"))
                //Get the current security context
                .flatMap(id -> ReactiveSecurityContextHolder.getContext()
                        .map(SecurityContext::getAuthentication)
                        // Check if the user requesting for their own resource or if the user is an Admin
                        .filter(authentication -> id.equals(authentication.getCredentials()) ||
                                authentication.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROLE_ADMIN.toString())))
                        .flatMap(authentication -> authorizedFlow(id))
                        //user is not authorized to access this resource
                        .switchIfEmpty(ResponseUtil.unauthorized("Unauthorized to access this resource")));
    }

    /**
     * This method is executed when the user is authorized
     */
    private Mono<ServerResponse> authorizedFlow(Integer id) {
        //Find the account by id
        return service.findById(id)
                // make the response
                .flatMap(account -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(AccountModel.builder()
                                .id(account.getId())
                                .name(account.getName())
                                .email(account.getEmail())
                                .build())
                        ))
                // User not found
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
