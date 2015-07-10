package com.thoughtworks.turtles.demos.vault.services;

import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.thoughtworks.turtles.demos.vault.util.FunctionalUtils.either;

@Service
public class SecretService {

    private final Map<String, Secret>                               secrets    = new HashMap<>();
    private final AtomicReference<Optional<AppIdAuthService.Token>> maybeToken = new AtomicReference<>(Optional.empty());
    private final AppIdAuthService appIdAuthService;

    @Autowired
    public SecretService(final AppIdAuthService appIdAuthService) {
        this.appIdAuthService = appIdAuthService;
    }

    public Optional<Map<String, ?>> getSecret(final String path) {
        final Optional<Secret> knownSecret = secret(path);
        return knownSecret.map(Secret::getValue);

    }

    private Optional<Secret> secret(final String path) {
        Optional<Secret> maybeSecret = Optional.ofNullable(secrets.get(path));
        return either(maybeSecret).or(getSecretFromStore(path));
    }

    private Optional<Secret> getSecretFromStore(final String path) {
        return getToken().flatMap(t -> getSecretWithToken(t.getValue(), path));
    }

    private Optional<AppIdAuthService.Token> getToken() {
        final Optional<AppIdAuthService.Token> token = maybeToken.get();
        return either(
                either(token.filter(AppIdAuthService.Token::isCurrent)).or(refreshToken(token.filter(AppIdAuthService.Token::isRenewable))))
                .or(createToken(token));
    }

    private Optional<AppIdAuthService.Token> refreshToken(final Optional<AppIdAuthService.Token> maybeToken) {
        return maybeToken.map(this::doRefresh);
    }

    private AppIdAuthService.Token doRefresh(final AppIdAuthService.Token t) {
        throw new UnsupportedOperationException(); // TODO
    }

    private Optional<Secret> getSecretWithToken(final String token, final String path) {
        throw new UnsupportedOperationException("NYI");
    }

    private Optional<AppIdAuthService.Token> createToken(final Optional<AppIdAuthService.Token> token) {
        throw new UnsupportedOperationException("NYI");
    }

    @Value
    private static class Secret {
        private final Map<String, ?> value;

        public boolean isExpired() {
            throw new UnsupportedOperationException("NYI");
        }
    }

}
