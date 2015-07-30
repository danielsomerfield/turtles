package com.thoughtworks.turtles.demos.blackbox.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfiguration {

    @Value("${blackbox.credentialsFilePath}")
    private String credentialsFilePath;

    public String getCredentialsFilePath() {
        return credentialsFilePath;
    }
}
