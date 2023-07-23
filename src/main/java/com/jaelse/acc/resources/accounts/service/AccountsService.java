package com.jaelse.acc.resources.accounts.service;

import com.jaelse.acc.api.authorization.models.AuthorizationModel;
import com.jaelse.acc.lib.dtos.accounts.CreateAccountDto;
import com.jaelse.acc.lib.dtos.accounts.SearchAccountDto;
import com.jaelse.acc.lib.dtos.accounts.UpdateAccountDto;
import com.jaelse.acc.lib.dtos.authorization.LoginDto;
import com.jaelse.acc.resources.accounts.domain.AccountEntity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountsService extends ReactiveUserDetailsService {

    Mono<AccountEntity> create(CreateAccountDto createAccountDto);

    Mono<AccountEntity> findById(Integer id);

    Mono<AccountEntity> update(Integer id, UpdateAccountDto updateAccountDto);

    Flux<AccountEntity> search(SearchAccountDto searchAccountDto);

    Mono<String> authenticate(LoginDto loginDto);
}
