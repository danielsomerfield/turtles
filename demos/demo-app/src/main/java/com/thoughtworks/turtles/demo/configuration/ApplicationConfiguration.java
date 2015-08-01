package com.thoughtworks.turtles.demo.configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.thoughtworks.turtles.demo.services.SecretService;
import com.thoughtworks.turtles.demo.services.vault.VaultSecretService;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Collections.singletonList;

@Configuration
@Slf4j
public class ApplicationConfiguration {

    public static final String MONGO_DB_NAME = "demo";

    @Bean
    public MongoCollection<Document> userCollection(MongoDatabase mongoDatabase) {
        return mongoDatabase.getCollection("user");
    }

    @Bean
    public MongoDatabase mongoDatabase(MongoClient mongoClient) {
        return mongoClient.getDatabase(MONGO_DB_NAME);
    }

    @Bean()
    public MongoClient mongoClient(MongoCredential credential) {
        return new MongoClient(new ServerAddress("localhost"), singletonList(credential));
    }

    @Bean
    public MongoCredential credential(SecretService secretService) {
        return secretService.getSecret("demo/mongo")
                .map(secret -> MongoCredential.createCredential("demo", MONGO_DB_NAME, secret.get("password").toCharArray()))
                .orElseGet(() -> {
                    log.warn("No secret fetched. Using default credentials");
                    return MongoCredential.createCredential("demo", MONGO_DB_NAME, new char[0]);
                });
//        return MongoCredential.createCredential("demo", MONGO_DB_NAME, "demo".toCharArray());
    }
}
