package com.thoughtworks.turtles.demos.vault.services;

import com.thoughtworks.turtles.demos.vault.configuration.SecurityConfiguration;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AppIdAuthService {

    private final SecurityConfiguration securityConfiguration;

    @Autowired
    public AppIdAuthService(final SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
        log.info("Starting with app id " + securityConfiguration.getAppId());
    }

    public Authentication authenticate() {
        throw new UnsupportedOperationException("NYI");
    }


    @Value
    public static class Authentication {
        private final Optional<Token> token;
    }

    @Value
    static class Token {
        private final String value;

        public boolean isCurrent() {
            throw new UnsupportedOperationException("NYI");
        }

        public boolean isRenewable() {
            return false;
        }
    }
}
