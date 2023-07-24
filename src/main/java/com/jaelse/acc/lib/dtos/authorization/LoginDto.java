package com.jaelse.acc.lib.dtos.authorization;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NonNull
    private String email;

    @NonNull
    private String password;
}
