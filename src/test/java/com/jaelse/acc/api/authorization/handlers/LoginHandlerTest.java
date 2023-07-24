package com.jaelse.acc.api.authorization.handlers;

import com.jaelse.acc.api.accounts.handlers.CreateAccountHandler;
import com.jaelse.acc.lib.dtos.accounts.CreateAccountDto;
import com.jaelse.acc.lib.dtos.authorization.LoginDto;
import com.jaelse.acc.resources.accounts.service.AccountsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@WebFluxTest
class LoginHandlerTest {
    @MockBean
    private AccountsService mockedAccountsService;

    private WebTestClient webTestClient;

    @BeforeEach
    void init() {
        LoginHandler testInstance = new LoginHandler(mockedAccountsService);
        RouterFunction<?> router = route()
                .POST("/v1/login", testInstance)
                .build();

        webTestClient = WebTestClient
                .bindToRouterFunction(router)
                .build();
    }

    @Test
    public void login_success() {

        var dto = LoginDto.builder()
                .email("asdf@gmail.com")
                .password("123").build();

        when(mockedAccountsService.authenticate(any(LoginDto.class)))
                .thenReturn(Mono.just("token"));

        webTestClient
                .post().uri("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody();
    }

    @Test
    public void login_unauthorized() {

        var dto = LoginDto.builder()
                .email("asdf@gmail.com")
                .password("123").build();

        when(mockedAccountsService.authenticate(any(LoginDto.class)))
                .thenReturn(Mono.empty());

        webTestClient
                .post().uri("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .isUnauthorized()
                .expectBody();
    }
}