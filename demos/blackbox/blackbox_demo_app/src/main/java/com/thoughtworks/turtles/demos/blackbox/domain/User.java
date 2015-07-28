package com.thoughtworks.turtles.demos.blackbox.domain;

import lombok.Value;

import java.util.Optional;

@Value
public class User {
    private final String            id;
    private final Optional<Account> account;
}
