package com.jaelse.acc.lib.dtos.accounts;

import lombok.Builder;

import javax.swing.text.html.Option;
import java.util.Optional;

@Builder
public class UpdateAccountDto {

    private final String name;
    private final String email;

    public Optional<String> maybeName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> maybeEmail() {
        return Optional.ofNullable(email);
    }
}
