package com.jaelse.acc.resources.accounts.service;

import com.jaelse.acc.lib.dtos.accounts.CreateAccountDto;
import com.jaelse.acc.lib.dtos.accounts.SearchAccountDto;
import com.jaelse.acc.lib.dtos.accounts.UpdateAccountDto;
import com.jaelse.acc.resources.accounts.domain.AccountEntity;
import com.jaelse.acc.resources.accounts.repository.AccountsRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class AccountsServiceImpl implements AccountsService {

    private final AccountsRepository repository;

    public AccountsServiceImpl(AccountsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<AccountEntity> create(CreateAccountDto createAccountDto) {
        return repository.create(
                        0L,
                        createAccountDto.getName(),
                        createAccountDto.getEmail())
                .flatMap(repository::findById);
    }

    @Override
    public Mono<AccountEntity> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Mono<AccountEntity> update(Integer id, UpdateAccountDto updateAccountDto) {
        return repository.findById(id)
                .flatMap(account -> {
                    updateAccountDto.maybeEmail().ifPresent(account::setEmail);
                    updateAccountDto.maybeName().ifPresent(account::setName);
                    return repository.update(account.getId(), account.getName(), account.getEmail());
                })
                .flatMap(repository::findById);
    }

    @Override
    public Flux<AccountEntity> search(SearchAccountDto searchAccountDto) {
        return repository.search(searchAccountDto.text()+":*");
    }
}
