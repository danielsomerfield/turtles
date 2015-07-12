package com.thoughtworks.turtles.demos.vault.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.turtles.demos.vault.configuration.SecurityConfiguration;
import com.thoughtworks.turtles.demos.vault.util.Http;
import com.thoughtworks.turtles.demos.vault.wireTypes.AppIdLoginWireType;
import com.thoughtworks.turtles.demos.vault.wireTypes.Authentication;
import com.thoughtworks.turtles.demos.vault.wireTypes.LoginTokenWireType;
import com.thoughtworks.turtles.demos.vault.wireTypes.Token;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

import static java.lang.String.format;

@Slf4j
@Service
public class AppIdAuthService {

    private final SecurityConfiguration securityConfiguration;

//    public static void main(String[] args) {
//        new AppIdAuthService(new SecurityConfiguration(){
//            @Override
//            public String getAppId() {
//                return "vault_demo";
//            }
//        }).authenticate();
//    }

    @Autowired
    public AppIdAuthService(final SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
        log.info(format("Starting with app id %s", securityConfiguration.getAppId()));
        //Normally you would NOT want to put this in the logs, but send it to a secure registry somewhere where a
        //security operator could associate the ids
        log.info(format("Starting with user id %s", securityConfiguration.getUserId()));
    }

    @SneakyThrows
    public Optional<Authentication> authenticate() {
        final HttpPost request = new HttpPost(format("%s/v1/auth/app-id/login", securityConfiguration.getVaultEndpoint()));
        String loginJSON = new ObjectMapper().writeValueAsString(new AppIdLoginWireType(securityConfiguration.getAppId(), securityConfiguration.getUserId()));
        log.info("Logging in with " + loginJSON);//TODO: remove this!!!!!
        request.setEntity(new StringEntity(loginJSON));
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
