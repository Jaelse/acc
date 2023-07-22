package com.jaelse.acc.lib.dtos.accounts;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CreateAccountDto {

    @NonNull
    private final String name;

    @NonNull
    private final String email;
}
