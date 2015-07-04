package com.thoughtworks.turtles.demos.vault.domain;

import lombok.Value;

@Value
public class Account {
    private final String id;
    private final String status;
}
