package com.jaelse.acc.api.accounts.handlers;

import com.jaelse.acc.lib.Role;
import com.jaelse.acc.lib.dtos.accounts.SearchAccountDto;
import com.jaelse.acc.resources.accounts.domain.AccountEntity;
import com.jaelse.acc.resources.accounts.service.AccountsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@WebFluxTest
class SearchAccountHandlerTest {
    @MockBean
    private AccountsService mockedAccountsService;

    private WebTestClient webTestClient;

    @BeforeEach
    void init() {
        SearchAccountHandler testInstance = new SearchAccountHandler(mockedAccountsService);
        RouterFunction<?> router = route()
                .GET("/v1/accounts", testInstance)
                .build();

        webTestClient = WebTestClient
                .bindToRouterFunction(router)
                .build();
    }


    @Test
    public void searchAccount_success() {

        when(mockedAccountsService.search(any(SearchAccountDto.class)))
                .thenReturn(Flux.just(givenAccountEntity()));

        webTestClient
                .method(HttpMethod.GET)
                .uri("/v1/accounts")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(SearchAccountDto.builder().text("jae").build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody();

    }

    @Test
    public void searchAccount_empty() {

        when(mockedAccountsService.search(any(SearchAccountDto.class)))
                .thenReturn(Flux.empty());

        webTestClient
                .method(HttpMethod.GET)
                .uri("/v1/accounts")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(SearchAccountDto.builder().text("jae").build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("accounts")
                .isArray()
                .equals(Collections.emptyList());

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