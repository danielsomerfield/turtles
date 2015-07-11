package com.thoughtworks.turtles.demos.vault.services;

import com.thoughtworks.turtles.demos.vault.configuration.SecurityConfiguration;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.lang.String.format;

@Slf4j
@Service
public class AppIdAuthService {

    private final SecurityConfiguration securityConfiguration;

    @Autowired
    public AppIdAuthService(final SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
        log.info(format("Starting with app id %s", securityConfiguration.getAppId()));
        //Normally you would NOT want to put this in the logs, but send it to a secure registry somewhere where a
        //security operator could associate the ids
        log.info(format("Starting with user id %s", securityConfiguration.getUserId()));
    }

    public Authentication authenticate() {
        final HttpUriRequest authRequest = new HttpPut(format("%s/v1/auth/app-id/login", securityConfiguration.getVaultEndpoint()));
        final CloseableHttpResponse authResponse = HttpClientBuilder.create().build().execute(authRequest);
        return new Authentication()
    }


    @Value
    public static class Authentication {
        private final Optional<Token> token;
        private final int returnCode;
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
