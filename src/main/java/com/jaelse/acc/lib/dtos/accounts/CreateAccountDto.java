package com.jaelse.acc.lib.dtos.accounts;

import lombok.*;

@Builder
public record CreateAccountDto(@NonNull
                               String name,
                               @NonNull
                               String email,
                               @NonNull
                               String password) {
}
