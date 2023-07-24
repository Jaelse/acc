package com.jaelse.acc.api.accounts.handlers;

import com.jaelse.acc.configurations.security.JwtUtil;
import com.jaelse.acc.lib.Role;
import com.jaelse.acc.lib.dtos.accounts.CreateAccountDto;
import com.jaelse.acc.resources.accounts.domain.AccountEntity;
import com.jaelse.acc.resources.accounts.service.AccountsService;
import io.jsonwebtoken.JwtBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@WebFluxTest
class GetAccountByIdHandlerTest {

    @MockBean
    private AccountsService mockedAccountsService;

    private Authentication auth;

    Authentication authentication = mock(Authentication.class);

    SecurityContext securityContext = mock(SecurityContext.class);
    private WebTestClient webTestClient;

    @BeforeEach
    void init() {
        auth = mock(Authentication.class);
        GetAccountByIdHandler testInstance = new GetAccountByIdHandler(mockedAccountsService);
        RouterFunction<?> router = route()
                .GET("/v1/accounts/{id}", testInstance)
                .build();

        webTestClient = WebTestClient
                .bindToRouterFunction(router)
                .build();
    }

    @Test
    public void getAccount_success() {

        when(auth.getCredentials()).thenReturn(1);
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(mockedAccountsService.findById(any(Integer.class)))
                .thenReturn(Mono.just(givenAccountEntity()));

        webTestClient
                .get()
                .uri("/v1/accounts/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody();

        verify(mockedAccountsService).findById(1);
    }


    @Test
    public void getAccount_unauthorized() {

        when(auth.getCredentials()).thenReturn(2);
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(mockedAccountsService.findById(any(Integer.class)))
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