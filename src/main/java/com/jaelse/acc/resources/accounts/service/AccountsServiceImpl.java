package com.jaelse.acc.resources.accounts.service;

import com.jaelse.acc.configurations.security.JwtUtil;
import com.jaelse.acc.configurations.security.PBKDF2Encoder;
import com.jaelse.acc.lib.Role;
import com.jaelse.acc.lib.dtos.accounts.CreateAccountDto;
import com.jaelse.acc.lib.dtos.accounts.SearchAccountDto;
import com.jaelse.acc.lib.dtos.accounts.UpdateAccountDto;
import com.jaelse.acc.lib.dtos.authorization.LoginDto;
import com.jaelse.acc.resources.accounts.domain.AccountEntity;
import com.jaelse.acc.resources.accounts.repository.AccountsRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;


@Service
public class AccountsServiceImpl implements AccountsService {

    private final AccountsRepository repository;

    private final PBKDF2Encoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public AccountsServiceImpl(AccountsRepository repository,
                               PBKDF2Encoder passwordEncoder,
                               JwtUtil jwtUtil) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<AccountEntity> create(CreateAccountDto createAccountDto) {
        var password = passwordEncoder.encode(createAccountDto.password());
        return repository.create(
                        0L,
                        createAccountDto.name(),
                        createAccountDto.email(),
                        password,
                        List.of(Role.ROLE_USER.toString()).toArray(new String[0]))
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
        return repository.search(searchAccountDto.text() + ":*");
    }

    @Override
    public Mono<String> authenticate(LoginDto loginDto) {
        return repository.findByEmail(loginDto.getEmail())
                .filter(validatePassword(loginDto))
                .map(jwtUtil::generateToken);
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return repository.findByEmail(username)
                .map(account -> account);
    }

    private Predicate<AccountEntity> validatePassword(LoginDto dto) {
        return userDetails -> passwordEncoder.encode(dto.getPassword())
                .equals(userDetails.getPassword());
    }


}
