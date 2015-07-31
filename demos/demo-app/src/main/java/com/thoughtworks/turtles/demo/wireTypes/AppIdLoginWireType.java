package com.thoughtworks.turtles.demo.wireTypes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class AppIdLoginWireType {
    private final String appId;
    private final String userId;

    @JsonProperty("app_id")
    public String getAppId() {
        return this.appId;
    }

    @JsonProperty("user_id")
    public String getUserId() {
        return this.userId;
    }

}
