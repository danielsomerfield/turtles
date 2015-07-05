package com.thoughtworks.turtles.demos.vault.configuration;

import com.mongodb.AuthenticationMechanism;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Arrays.asList;

@Configuration
public class ApplicationConfiguration {

    public static final String MONGO_DB_NAME = "vault-demo";

    @Bean
    public MongoCollection<Document> userCollection(MongoDatabase mongoDatabase) {
        return mongoDatabase.getCollection("user");
    }

    @Bean
    public MongoDatabase mongoDatabase(MongoClient mongoClient) {
        return mongoClient.getDatabase(MONGO_DB_NAME);
    }

    @Bean
    public MongoClient mongoClient(MongoCredential credential) {
        return new MongoClient(new ServerAddress("localhost"), asList(credential));
    }

    @Bean
    public MongoCredential credential() {
        return MongoCredential.createCredential("vault-demo", MONGO_DB_NAME, "vault-demo".toCharArray());
    }
}
