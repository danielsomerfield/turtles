package com.thoughtworks.turtles.demo.services;

import java.util.Map;
import java.util.Optional;

public interface SecretService {
    Optional<Map<String, String>> getSecret(String path);
}
