package com.thoughtworks.turtles.demo.services;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class PropertiesFileSecretService implements SecretService {
    private final Secrets       secrets;
    private final SecretCleaner secretCleaner;

    public PropertiesFileSecretService(
            final PropertiesFileSecretServiceConfiguration propertiesFileSecretServiceConfiguration,
            final SecretCleaner secretCleaner

    ) {
        this.secretCleaner = secretCleaner;
        try (FileInputStream in = new FileInputStream(propertiesFileSecretServiceConfiguration.getCredentialsFilePath())) {
            this.secrets = new ObjectMapper().readValue(
                    in, Secrets.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.secretCleaner.clean();
    }

    @Override
    public Optional<Map<String, String>> getSecret(final String path) {
        return Optional.ofNullable(secrets.getSecrets().get(path));
    }

    @Value
    @JsonDeserialize(using = SecretsDeserializer.class)
    public static class Secrets {
        Map<String, Map<String, String>> secrets;
    }

    public static class SecretsDeserializer extends JsonDeserializer<Secrets> {

        public static final TypeReference MAP_TYPE = new TypeReference<Map<String, String>>() {
        };

        @Override
        public Secrets deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
            Map<String, Map<String, String>> secrets = new HashMap<>();

            if (jp.nextToken() == JsonToken.FIELD_NAME) {
                String path = jp.getCurrentName();
                if (jp.nextToken() == JsonToken.START_OBJECT) {
                    secrets.put(path, parseValues(jp));
                }
                return new Secrets(secrets);
            }
            throw new IOException("Failed to parse.");
        }

        private Map<String, String> parseValues(final JsonParser jp) throws IOException {
            return jp.readValueAs(MAP_TYPE);
        }
    }
}
