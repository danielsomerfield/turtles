package com.thoughtworks.turtles.demo.wireTypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthWireType {
    private final String       clientToken;
    private final List<String> policies;

    @JsonCreator
    public AuthWireType(
            @JsonProperty("client_token") final String clientToken,
            @JsonProperty("policies") final List<String> policies) {
        this.clientToken = clientToken;
        this.policies = policies;
    }
}
