package com.thoughtworks.turtles.demos.blackbox.services;

import com.thoughtworks.turtles.demos.blackbox.configuration.SecurityConfiguration;
import com.thoughtworks.turtles.demos.blackbox.wireTypes.Token;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.System.currentTimeMillis;

@Service
@Slf4j
public class SecretService {

    private final Map<String, Secret>              secrets    = new HashMap<>();
    private final AtomicReference<Optional<Token>> maybeToken = new AtomicReference<>(Optional.empty());
    private final SecurityConfiguration securityConfiguration;

    @Autowired
    public SecretService(final SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
    }

    public Optional<Map<String, String>> getSecret(final String path) {
        throw new UnsupportedOperationException("NYI");
    }

    private static long currentTimeInSeconds() {
        return currentTimeMillis() / 1000;
    }

    @Value
    private static class Secret {
        private static final long SLOP_FACTOR = 60;
        private final String              leaseId;
        private final Map<String, String> data;
        private final long                expirationTime;

        public boolean isCurrent() {
            return (currentTimeInSeconds()) + SLOP_FACTOR < expirationTime;
        }
    }

}
