package com.thoughtworks.turtles.demos.blackbox.e2e;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SmokeIntegrationTest {

    @Test
    public void testHealthCheck() throws Exception {
        try (final CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet get = new HttpGet("http://localhost:18080/health");
            final CloseableHttpResponse response = client.execute(get);
            assertThat(response.getStatusLine().getStatusCode(), is(200));
        }
    }

    @Test
    public void testInsertAndFind() throws Exception {

        JSONObject accountObject = new JSONObject()
                .put("id", UUID.randomUUID())
                .put("status", "new");

        try (final CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpPost post = new HttpPost("http://localhost:18080/users");
            final BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
            basicHttpEntity.setContentType("application/json");
            basicHttpEntity.setContent(IOUtils.toInputStream(accountObject.toString()));
            post.setEntity(basicHttpEntity);
            final CloseableHttpResponse response = client.execute(post);
            assertThat(response.getStatusLine().getStatusCode(), is(201));
            System.out.println(Arrays.asList(response.getAllHeaders()));
        }
    }

}
