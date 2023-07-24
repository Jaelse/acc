package com.jaelse.acc.lib.dtos.accounts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.swing.text.html.Option;
import java.util.Optional;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountDto {

    private  String name;
    private  String email;

    public Optional<String> maybeName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> maybeEmail() {
        return Optional.ofNullable(email);
    }
}
