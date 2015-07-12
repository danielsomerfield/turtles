package com.thoughtworks.turtles.demos.vault.wireTypes;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Value
@Slf4j
public class Authentication {
    private final Optional<Token> token;
    private final int             returnCode;

    public boolean isSuccessful() {
        return returnCode == 200;
    }
}
