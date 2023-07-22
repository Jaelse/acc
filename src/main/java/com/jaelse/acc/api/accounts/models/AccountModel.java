package com.jaelse.acc.api.accounts.models;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class AccountModel {
    private Integer id;
    private String name;
    private String email;
}
