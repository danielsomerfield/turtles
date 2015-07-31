package com.thoughtworks.turtles.demo.domain;

import lombok.Value;

@Value
public class Account {
    private final String id;
    private final String status;
}
