package com.thoughtworks.turtles.demos.blackbox.services;

import com.thoughtworks.turtles.demos.blackbox.configuration.SecurityConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

@Service
@Slf4j
public class SecretService {

    private final Properties secrets = new Properties();

    @Autowired
    public SecretService(final SecurityConfiguration securityConfiguration) {
        try {
            secrets.load(new FileInputStream(securityConfiguration.getCredentialsFilePath()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load secret properties.", e);
        }
        //TODO: delete secret properties
    }

    public Optional<String> getSecret(final String path) {
        return Optional.ofNullable(secrets.getProperty(path));
    }

}
