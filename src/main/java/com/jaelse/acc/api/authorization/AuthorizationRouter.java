package com.jaelse.acc.api.authorization;

import com.jaelse.acc.api.authorization.handlers.LoginHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthorizationRouter {

    private final LoginHandler loginHandler;

    public AuthorizationRouter(LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> authorizationRoutes() {
        return route()
                .POST("/v1/login", loginHandler)
                .build();
    }
}
