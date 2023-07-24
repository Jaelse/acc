package com.jaelse.acc.api.accounts.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class SearchAccountsModel {
    private List<AccountModel> accounts;
}
