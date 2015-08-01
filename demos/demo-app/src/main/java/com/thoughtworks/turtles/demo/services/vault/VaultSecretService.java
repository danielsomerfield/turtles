package com.thoughtworks.turtles.demo.services.vault;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.turtles.demo.services.SecretService;
import com.thoughtworks.turtles.demo.util.Http;
import com.thoughtworks.turtles.demo.wireTypes.Authentication;
import com.thoughtworks.turtles.demo.wireTypes.SecretWireType;
import com.thoughtworks.turtles.demo.wireTypes.Token;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.thoughtworks.turtles.demo.util.FunctionalUtils.either;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;

@Slf4j
public class VaultSecretService implements SecretService {

    private final Map<String, Secret>              secrets    = new HashMap<>();
    private final AtomicReference<Optional<Token>> maybeToken = new AtomicReference<>(Optional.empty());
    private final AppIdAuthService   appIdAuthService;
    private final VaultConfiguration vaultConfiguration;

    @Autowired
    public VaultSecretService(final AppIdAuthService appIdAuthService, final VaultConfiguration vaultConfiguration) {
        this.appIdAuthService = appIdAuthService;
        this.vaultConfiguration = vaultConfiguration;
    }

    @Override
    public Optional<Map<String, String>> getSecret(final String path) {
        final Optional<Secret> cachedSecret;

        synchronized (secrets) {
            cachedSecret = Optional.ofNullable(secrets.get(path));
        }

        Optional<Secret> maybeCurrentSecret = either(cachedSecret.filter(Secret::isCurrent)).or(renewSecret(cachedSecret));
        final Optional<Secret> secret = either(maybeCurrentSecret).or((getSecretFromStore(path)));

        secret.ifPresent(s -> {
            synchronized (secrets) {
                secrets.put(path, s);
            }
        });
        return secret.map(Secret::getData);
    }

    private Optional<Secret> renewSecret(final Optional<Secret> knownSecret) {
        return knownSecret.filter(Secret::isCurrent); //TODO: not supporting renewal right now
    }

    private Optional<Secret> getSecretFromStore(final String path) {
        return getCachedToken().flatMap(t -> getSecretWithToken(t.getValue(), path));
    }

    private Optional<Token> getCachedToken() {
        return maybeToken.updateAndGet(maybeToken -> maybeToken.isPresent() ? maybeToken : createToken());
    }

    private Optional<Secret> getSecretWithToken(final String token, final String path) {
        final HttpGet get = new HttpGet(format("%s/v1/secret/%s", vaultConfiguration.getVaultEndpoint(), path));
        get.setHeader("X-Vault-Token", token);
        return Http.doRequest(get).flatMap(this::parseSecret).map(w ->
                new Secret(
                        w.getLeaseId(),
                        w.getData(),
                        currentTimeInSeconds() + w.getLeaseDuration()));
    }

    private static long currentTimeInSeconds() {
        return currentTimeMillis() / 1000;
    }

    private Optional<SecretWireType> parseSecret(final CloseableHttpResponse resp) {
        try {
            final String content = IOUtils.toString(resp.getEntity().getContent());
            return Optional.of(new ObjectMapper().readValue(content, SecretWireType.class));
        } catch (IOException e) {
            log.warn("Failed to parse secret", e);
            return Optional.empty();
        }
    }

    private Optional<Token> createToken() {
        return appIdAuthService.authenticate()
                .filter(Authentication::isSuccessful)
                .flatMap(Authentication::getToken);
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
