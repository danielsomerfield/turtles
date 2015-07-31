package com.thoughtworks.turtles.demo.wireTypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.Map;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecretWireType {
    private final String              leaseId;
    private final boolean             renewable;
    private final long                leaseDuration;
    private final Map<String, String> data;
    //Add error list

    @JsonCreator
    public SecretWireType(
            @JsonProperty("lease_id") final String leaseId,
            @JsonProperty("renewable") final boolean renewable,
            @JsonProperty("lease_duration") final long leaseDuration,
            @JsonProperty("data") final Map<String, String> data
    ) {
        this.leaseId = leaseId;
        this.renewable = renewable;
        this.leaseDuration = leaseDuration;
        this.data = data;
    }
}
