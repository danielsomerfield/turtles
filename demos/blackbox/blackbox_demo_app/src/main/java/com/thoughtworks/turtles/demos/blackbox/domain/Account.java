package com.thoughtworks.turtles.demos.blackbox.domain;

import lombok.Value;

@Value
public class Account {
    private final String id;
    private final String status;
}
