package com.jaelse.acc.api.accounts;

import com.jaelse.acc.api.accounts.handlers.CreateAccountHandler;
import com.jaelse.acc.api.accounts.handlers.GetAccountByIdHandler;
import com.jaelse.acc.api.accounts.handlers.UpdateAccountByIdHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AccountRouter {

    private final CreateAccountHandler createAccountHandler;
    private final GetAccountByIdHandler getAccountByIdHandler;
    private final UpdateAccountByIdHandler updateAccountByIdHandler;

    public AccountRouter(CreateAccountHandler createAccountHandler, GetAccountByIdHandler getAccountByIdHandler, UpdateAccountByIdHandler updateAccountByIdHandler) {
        this.createAccountHandler = createAccountHandler;
        this.getAccountByIdHandler = getAccountByIdHandler;
        this.updateAccountByIdHandler = updateAccountByIdHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> accountRoutes() {
        return route()
                .POST("/v1/accounts", createAccountHandler)
                .GET("/v1/accounts/{id}", getAccountByIdHandler)
                .PUT("/v1/accounts/{id}", updateAccountByIdHandler)
                .build();
    }
}
