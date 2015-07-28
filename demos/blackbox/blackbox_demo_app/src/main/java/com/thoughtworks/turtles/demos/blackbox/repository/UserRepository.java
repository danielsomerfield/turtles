package com.thoughtworks.turtles.demos.blackbox.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.thoughtworks.turtles.demos.blackbox.domain.Account;
import com.thoughtworks.turtles.demos.blackbox.domain.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class UserRepository {

    private final MongoCollection<Document> userCollection;

    @Autowired
    public UserRepository(final MongoCollection<Document> userCollection) {
        this.userCollection = userCollection;
    }

    public Optional<User> findUserById(final String userId) {
        return userFromRecord(userCollection.find(eq("_id", new ObjectId(userId))).first());
    }

    private Optional<User> userFromRecord(final Document record) {
        return Optional.ofNullable(record)
                .map(r -> new User(
                        record.getString("_id"),
                        accountFromRecord(record)
                ));
    }

    private Optional<Account> accountFromRecord(final Document record) {
        return Optional.ofNullable(record)
                .map(r -> record.get("account", Document.class))
                .map(account -> new Account(
                                account.getString("id"),
                                account.getString("status")
                        )
                );
    }

    public String insertUser(final Account account) {
        ObjectId id = new ObjectId();
        User user = new User(id.toString(), Optional.of(new Account(id.toString(), "good_standing")));
        userCollection.insertOne(recordFromUser(user));
        return id.toString();
    }

    @SuppressWarnings("unchecked")
    private Document recordFromUser(final User user) {
        return new Document(BasicDBObjectBuilder
                .start("_id", user.getId())
                .add("account", recordFromAccount(user.getAccount())).get().toMap());
    }

    private DBObject recordFromAccount(final Optional<Account> maybeAccount) {
        return maybeAccount.map(account -> BasicDBObjectBuilder
                .start("id", account.getId())
                .add("status", account.getStatus()).get())
                .orElseGet(BasicDBObject::new);

    }
}
