package com.thoughtworks.turtles.demos.vault.services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.turtles.demos.vault.configuration.SecurityConfiguration;
import com.thoughtworks.turtles.demos.vault.util.Http;
import com.thoughtworks.turtles.demos.vault.wireTypes.Authentication;
import com.thoughtworks.turtles.demos.vault.wireTypes.Token;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.thoughtworks.turtles.demos.vault.util.FunctionalUtils.either;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;

@Service
@Slf4j
public class SecretService {

    private final Map<String, Secret>              secrets    = new HashMap<>();
    private final AtomicReference<Optional<Token>> maybeToken = new AtomicReference<>(Optional.empty());
    private final AppIdAuthService      appIdAuthService;
    private final SecurityConfiguration securityConfiguration;

    @Autowired
    public SecretService(final AppIdAuthService appIdAuthService, final SecurityConfiguration securityConfiguration) {
        this.appIdAuthService = appIdAuthService;
        this.securityConfiguration = securityConfiguration;
    }

    public Optional<Map<String, String>> getSecret(final String path) {
        final Optional<Secret> cachedSecret;

        synchronized (secrets) {
            cachedSecret = Optional.ofNullable(secrets.get(path));
        }

        Optional<Secret> maybeCurrentSecret = either(cachedSecret.filter(Secret::isCurrent)).or(renewSecret(cachedSecret));
        log.info("maybeCurrentSecret " + maybeCurrentSecret);

        final Optional<Secret> secret = either(maybeCurrentSecret).or((getSecretFromStore(path)));
        log.info("Got secret " + secret);


        secret.ifPresent(s -> {
            synchronized (secrets) {
                secrets.put(path, s);
            }
        });
        return secret.map(Secret::getData);
    }

    private Optional<Secret> renewSecret(final Optional<Secret> knownSecret) {
        log.info("Secret is present and current? " + knownSecret.map(Secret::isCurrent));
        return knownSecret.filter(Secret::isCurrent); //TODO: not supporting renewal right now
    }

    private Optional<Secret> getSecretFromStore(final String path) {
        return getToken().flatMap(t -> getSecretWithToken(t.getValue(), path));
    }

    private Optional<Token> getToken() {
        return maybeToken.updateAndGet(maybeToken -> maybeToken.isPresent() ? maybeToken : createToken());
    }

    /*
    {
        "lease_id":"secret/foo/5958b11a-d996-ec87-725e-743beb2e1416",
        "renewable":false,
        "lease_duration":2592000,
        "data":{"value":"bar"},
        "auth":null}
     */
    private Optional<Secret> getSecretWithToken(final String token, final String path) {
        final HttpGet get = new HttpGet(format("%s/v1/secret/%s", securityConfiguration.getVaultEndpoint(), path));
        get.setHeader("X-Vault-Token", token);
        log.info("Doing request " + get);
        log.info(" with header " + get.getFirstHeader("X-Vault-Token"));
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
            log.info("Parsing secret " + content);
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

    @Value
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SecretWireType {
        private final String              leaseId;
        private final boolean             renewable;
        private final long                leaseDuration;
        private final Map<String, String> data;
        //Add error list

        @JsonCreator
        public SecretWireType(
                @JsonProperty("lease_id") final String leaseId,
                @JsonProperty("renewable") final boolean renewable,
                @JsonProperty("lease_duration") final long leaseDuration,
                @JsonProperty("data") final Map<String, String> data
        ) {
            this.leaseId = leaseId;
            this.renewable = renewable;
            this.leaseDuration = leaseDuration;
            this.data = data;
        }
    }
}
