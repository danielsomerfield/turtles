package com.thoughtworks.turtles.demos.vault.wireTypes;

import lombok.Value;

@Value
public class AccountWireType {
    private final String id;
    private final String status;
}
