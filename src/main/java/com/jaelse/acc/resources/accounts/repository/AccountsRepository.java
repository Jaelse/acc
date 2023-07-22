package com.jaelse.acc.resources.accounts.repository;

import com.jaelse.acc.resources.accounts.domain.AccountEntity;
import lombok.NonNull;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccountsRepository extends ReactiveCrudRepository<AccountEntity, Integer> {

    @Query(value = "INSERT INTO accounts(version, name, email) VALUES ($1, $2, $3) RETURNING id")
    Mono<Integer> create(Long version, String name, String email);

    @Query(value = "SELECT ID, name, email FROM accounts WHERE ID = :id")
    Mono<AccountEntity> findById(@NonNull Integer id);

    @Query(value = "UPDATE accounts SET name=:name, email=:email WHERE id=:id RETURNING *")
    Mono<Integer> update(@NonNull Integer id, @NonNull String name, @NonNull String email);

    @Query(value = "SELECT * FROM accounts WHERE to_tsvector(name) @@ to_tsquery(:text)")
    Flux<AccountEntity> search(String text);
}
