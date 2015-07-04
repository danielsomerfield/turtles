package com.thoughtworks.turtles.demos.vault.wireTypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class AccountWireType {
    private final String id;
    private final String status;

    @JsonCreator
    public AccountWireType(@JsonProperty("id") final String id, @JsonProperty("status") final String status) {
        this.id = id;
        this.status = status;
    }
}
