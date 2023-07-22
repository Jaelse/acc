package com.jaelse.acc.resources.accounts.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(value = "accounts")
public class AccountEntity {

    @Id
    @Column("id")
    private Integer id;

    @Version
    private Long version;

    @NonNull
    @Column("name")
    private String name;

    @NonNull
    @Column("email")
    private String email;
}
