package com.thoughtworks.turtles.demo.controllers;

import com.thoughtworks.turtles.demo.domain.Account;
import com.thoughtworks.turtles.demo.services.vault.AccountService;
import com.thoughtworks.turtles.demo.wireTypes.AccountWireType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.String.format;
import static java.net.URI.create;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(final AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "/users/{userid}/account", produces = "application/json", method = GET)
    public ResponseEntity<?> getAccountStatus(@PathVariable("userid") final String userId) {
        return accountService.getAccountForUserId(userId)
                .map(account -> ResponseEntity.<Object>ok(toWireType(account)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(""));
    }

    @RequestMapping(value = "/users", produces = "application/json", consumes = "application/json", method = POST)
    public ResponseEntity<String> registerUser(@RequestBody AccountWireType account) {
        final String userId = accountService.registerUser(fromWireType(account));
        return ResponseEntity.created(create(format("/user/%s", userId))).body(userId);
    }

    private AccountWireType toWireType(final Account account) {
        return new AccountWireType(
                account.getId(),
                account.getStatus()
        );
    }

    private Account fromWireType(final AccountWireType accountWireType) {
        return new Account(
                accountWireType.getId(),
                accountWireType.getStatus()
        );
    }

}
