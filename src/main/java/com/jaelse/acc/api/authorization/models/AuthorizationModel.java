package com.jaelse.acc.api.authorization.models;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthorizationModel {
    private final String token;
}
