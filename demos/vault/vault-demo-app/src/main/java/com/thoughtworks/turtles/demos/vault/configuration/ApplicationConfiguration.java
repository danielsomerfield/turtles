package com.thoughtworks.turtles.demos.vault.configuration;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public MongoCollection<Document> accountCollection(MongoDatabase mongoDatabase) {
        return mongoDatabase.getCollection("account");
    }

    @Bean
    public MongoDatabase mongoDatabase(MongoClient mongoClient) {
        return mongoClient.getDatabase("vault-demo");
    }

    @Bean
    public MongoClient mongoClient() {
        return new MongoClient();
    }
}
