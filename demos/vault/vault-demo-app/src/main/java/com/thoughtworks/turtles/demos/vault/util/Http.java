package com.thoughtworks.turtles.demos.vault.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.Optional;

public class Http {
    public static Optional<CloseableHttpResponse> doRequest(HttpUriRequest request) {
        try {
            return Optional.of(HttpClientBuilder.create().build().execute(request));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
