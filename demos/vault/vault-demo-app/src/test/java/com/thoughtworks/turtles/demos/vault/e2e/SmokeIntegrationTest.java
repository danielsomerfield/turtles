package com.thoughtworks.turtles.demos.vault.e2e;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SmokeIntegrationTest {

    @Test
    public void testHealthCheck() throws Exception {
        try(final CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet get = new HttpGet("http://localhost:18080/health");
            final CloseableHttpResponse response = client.execute(get);
            assertThat(response.getStatusLine().getStatusCode(), is(200));
        }
    }
}
