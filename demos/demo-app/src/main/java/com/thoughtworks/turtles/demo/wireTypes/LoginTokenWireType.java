package com.thoughtworks.turtles.demo.wireTypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginTokenWireType {
    private final String       leaseId;
    private final boolean      renewable;
    private final long         leaseDuration;
    private final AuthWireType authWireType;

    @JsonCreator
    public LoginTokenWireType(
            @JsonProperty("lease_id") final String leaseId,
            @JsonProperty("renewable") final boolean renewable,
            @JsonProperty("lease_duration") final long leaseDuration,
            @JsonProperty("auth") final AuthWireType authWireType
    ) {
        this.leaseId = leaseId;
        this.renewable = renewable;
        this.leaseDuration = leaseDuration;
        this.authWireType = authWireType;
    }
}
