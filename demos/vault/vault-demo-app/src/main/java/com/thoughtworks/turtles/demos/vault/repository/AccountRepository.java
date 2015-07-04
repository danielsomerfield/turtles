package com.thoughtworks.turtles.demos.vault.repository;

import com.mongodb.client.MongoCollection;
import com.thoughtworks.turtles.demos.vault.domain.Account;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class AccountRepository {

    private final MongoCollection<Document> userCollection;

    @Autowired
    public AccountRepository(final MongoCollection<Document> userCollection) {
        this.userCollection = userCollection;
    }

    public Optional<Account> findAccountByUserId(final String userId) {
        return createAccount(userCollection.find(eq("_id", new ObjectId(userId))).first());
    }

    private Optional<Account> createAccount(final Document record) {
        return Optional.ofNullable(record)
                .map(r -> record.get("account", Document.class))
                .map(account -> new Account(
                                account.getString("id"),
                                account.getString("account_status")
                        )
                );
    }
}
