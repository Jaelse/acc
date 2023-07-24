package com.jaelse.acc.api.accounts.handlers;

import com.jaelse.acc.api.accounts.models.AccountModel;
import com.jaelse.acc.lib.Role;
import com.jaelse.acc.lib.dtos.accounts.UpdateAccountDto;
import com.jaelse.acc.resources.accounts.domain.AccountEntity;
import com.jaelse.acc.resources.accounts.service.AccountsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@WebFluxTest
class UpdateAccountByIdHandlerTest {
    @MockBean
    private AccountsService mockedAccountsService;

    private Authentication auth;

    Authentication authentication = mock(Authentication.class);

    SecurityContext securityContext = mock(SecurityContext.class);
    private WebTestClient webTestClient;

    @BeforeEach
    void init() {
        auth = mock(Authentication.class);
        UpdateAccountByIdHandler testInstance = new UpdateAccountByIdHandler(mockedAccountsService);
        RouterFunction<?> router = route()
                .PUT("/v1/accounts/{id}", testInstance)
                .build();

        webTestClient = WebTestClient
                .bindToRouterFunction(router)
                .build();
    }

    @Test
    public void updateAccount_success() {

        when(auth.getCredentials()).thenReturn(1);
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(mockedAccountsService.update(any(Integer.class), any(UpdateAccountDto.class)))
                .thenReturn(Mono.just(givenAccountEntity()));

        webTestClient
                .put()
                .uri("/v1/accounts/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(BodyInserters.fromValue(UpdateAccountDto.builder()
                        .email("a@gmail.com")
                        .build()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody();
    }


    @Test
    public void updateAccount_unauthorized() {

        when(auth.getCredentials()).thenReturn(2);
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(mockedAccountsService.update(any(Integer.class), any(UpdateAccountDto.class)))
                .thenReturn(Mono.just(givenAccountEntity()));

        webTestClient
                .get()
                .uri("/v1/accounts/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody();
    }


    AccountEntity givenAccountEntity() {
        return AccountEntity.builder()
                .id(1)
                .version(0L)
                .name("thename")
                .email("email@gmail.com")
                .password("jubrish")
                .roles(List.of(Role.ROLE_USER))
                .build();
    }
}