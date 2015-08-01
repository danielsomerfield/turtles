package com.thoughtworks.turtles.demo.services.vault;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.turtles.demo.util.Http;
import com.thoughtworks.turtles.demo.wireTypes.AppIdLoginWireType;
import com.thoughtworks.turtles.demo.wireTypes.Authentication;
import com.thoughtworks.turtles.demo.wireTypes.LoginTokenWireType;
import com.thoughtworks.turtles.demo.wireTypes.Token;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;

import static java.lang.String.format;

@Slf4j
public class AppIdAuthService {

    private final VaultConfiguration vaultConfiguration;

    @Autowired
    public AppIdAuthService(final VaultConfiguration vaultConfiguration) {
        this.vaultConfiguration = vaultConfiguration;
        log.info(format("Starting with app id %s", vaultConfiguration.getAppId()));
        //Normally you would NOT want to put this in the logs, but send it to a secure registry somewhere where a
        //security operator could associate the ids
        log.info(format("Starting with user id %s", vaultConfiguration.getUserId()));
    }

    @SneakyThrows
    public Optional<Authentication> authenticate() {
        final HttpPost request = new HttpPost(format("%s/v1/auth/app-id/login", vaultConfiguration.getVaultEndpoint()));
        request.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(new AppIdLoginWireType(vaultConfiguration.getAppId(), vaultConfiguration.getUserId()))));
        request.setHeader("Content-type", "application/json");
        return Http.doRequest(request)
                .flatMap(this::toAuth);
    }

    private Optional<Authentication> toAuth(CloseableHttpResponse response) {
        try {
            int returnCode = response.getStatusLine().getStatusCode();
            Optional<LoginTokenWireType> maybeToken = returnCode == 200 ?
                    Optional.of(
                            new ObjectMapper().readValue(response.getEntity().getContent(), LoginTokenWireType.class)) :
                    Optional.empty();
            return Optional.of(maybeToken.map(t -> createAuthentication(t, returnCode))
                    .orElse(new Authentication(Optional.empty(), returnCode)));
        } catch (IOException e) {
            log.warn("Failed to parse auth", e);
            return Optional.empty();
        }
    }

    private Authentication createAuthentication(final LoginTokenWireType loginTokenWireType, int returnCode) {
        return new Authentication(
                Optional.of(new Token(loginTokenWireType.getAuthWireType().getClientToken())), returnCode
        );
    }
}
