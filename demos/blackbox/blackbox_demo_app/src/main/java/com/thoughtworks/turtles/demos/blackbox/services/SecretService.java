package com.thoughtworks.turtles.demos.blackbox.services;

import com.thoughtworks.turtles.demos.blackbox.configuration.SecurityConfiguration;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Service
@Slf4j
public class SecretService {

    private final Map<String, Secret> secrets = new HashMap<>();

    @Autowired
    public SecretService(final SecurityConfiguration securityConfiguration) {
        throw new UnsupportedOperationException("NYI");
    }

    private Properties getSecretProperties() {
        throw new UnsupportedOperationException(); // TODO
    }


    public Optional<Map<String, String>> getSecret(final String path) {
        return Optional.ofNullable(secrets.get(path).getData());
    }

    @Value
    private static class Secret {
        private final Map<String, String> data;
    }

}
