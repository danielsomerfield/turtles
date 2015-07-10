package com.thoughtworks.turtles.demos.vault.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfiguration {

    @Value("${vault.appId}")
    private String appId;

    public String getAppId() {
        return appId;
    }
}
