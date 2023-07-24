package com.jaelse.acc.api.accounts.handlers;

import com.jaelse.acc.lib.Role;
import com.jaelse.acc.lib.dtos.accounts.CreateAccountDto;
import com.jaelse.acc.resources.accounts.domain.AccountEntity;
import com.jaelse.acc.resources.accounts.service.AccountsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@WebFluxTest
public class CreateAccountHandlerTest {

    @MockBean
    private AccountsService mockedAccountsService;

    private WebTestClient webTestClient;

    @BeforeEach
    void init() {
        CreateAccountHandler testInstance = new CreateAccountHandler(mockedAccountsService);
        RouterFunction<?> router = route()
                .POST("/v1/accounts", testInstance)
                .build();

        webTestClient = WebTestClient
                .bindToRouterFunction(router)
                .build();
    }

    @Test
    public void createAccount_success() {

        var dto = givenCreateAccountDto();

        when(mockedAccountsService.create(any(CreateAccountDto.class)))
                .thenReturn(Mono.just(givenAccountEntity()));

        webTestClient
                .post().uri("/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody();
    }


    @Test
    public void createAccount_empty_body() {

        var dto = givenCreateAccountDto();

        when(mockedAccountsService.create(any(CreateAccountDto.class)))
                .thenReturn(Mono.just(givenAccountEntity()));

        webTestClient
                .post().uri("/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is5xxServerError()
                .expectBody();
    }

    @Test
    public void createAccount_conflict() {

        var dto = givenCreateAccountDto();

        when(mockedAccountsService.create(any(CreateAccountDto.class)))
                .thenReturn(Mono.error(mock(DuplicateKeyException.class)));

        webTestClient
                .post().uri("/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody();
    }

    @Test
    public void createAccount_unknown_error() {

        var dto = givenCreateAccountDto();

        when(mockedAccountsService.create(any(CreateAccountDto.class)))
                .thenReturn(Mono.empty());

        webTestClient
                .post().uri("/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .is5xxServerError()
                .expectBody();
    }


    CreateAccountDto givenCreateAccountDto() {
        return new CreateAccountDto("somename", "email", "jubrish");
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