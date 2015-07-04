package com.thoughtworks.turtles.demos.vault.repository;

import com.mongodb.client.MongoCollection;
import com.thoughtworks.turtles.demos.vault.domain.Account;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class AccountRepository {

    private final MongoCollection<Document> accountCollection;

    @Autowired
    public AccountRepository(final MongoCollection<Document> accountCollection) {
        this.accountCollection = accountCollection;
    }


    public Account findAccountByUserId(final String userId) {
        return createAccount(accountCollection.find(eq("_id", userId)).first());
    }

    private Account createAccount(final Document record) {
        return new Account(
                record.getString("_id"),
                record.getString("account_status")
        );
    }
}
