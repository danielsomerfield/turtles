package com.thoughtworks.turtles.demo.services.keywhiz;

import com.thoughtworks.turtles.demo.services.SecretService;

import java.util.Map;
import java.util.Optional;

public class KeywhizSecretService implements SecretService {

    public KeywhizSecretService(final KeywhizConfiguration keywhizConfiguration) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public Optional<Map<String, String>> getSecret(final String path) {
        throw new UnsupportedOperationException(); // TODO
    }
}
