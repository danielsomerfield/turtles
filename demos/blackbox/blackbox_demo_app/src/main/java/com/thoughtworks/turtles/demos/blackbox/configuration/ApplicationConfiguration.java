package com.thoughtworks.turtles.demos.blackbox.configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.thoughtworks.turtles.demos.blackbox.services.SecretService;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Collections.singletonList;

@Configuration
@Slf4j
public class ApplicationConfiguration {

    public static final String MONGO_DB_NAME = "blackbox-demo";

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
        return secretService.getSecret("blackbox-demo/mongo")
                .map(secret -> MongoCredential.createCredential("blackbox-demo", MONGO_DB_NAME, secret.toCharArray()))
                .orElseGet(() -> {
                    log.warn("No secret fetched. Using default credentials");
                    return MongoCredential.createCredential("blackbox-demo", MONGO_DB_NAME, new char[0]);
                });
//        return MongoCredential.createCredential("blackbox-demo", MONGO_DB_NAME, "blackbox-demo".toCharArray());
    }

}
