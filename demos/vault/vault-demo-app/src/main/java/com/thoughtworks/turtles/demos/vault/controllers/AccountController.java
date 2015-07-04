package com.thoughtworks.turtles.demos.vault.controllers;

import com.thoughtworks.turtles.demos.vault.domain.Account;
import com.thoughtworks.turtles.demos.vault.services.AccountService;
import com.thoughtworks.turtles.demos.vault.wireTypes.AccountWireType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(final AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "/user/{userid}/account", produces = "application/json", method = GET)
    public ResponseEntity<?> getAccountStatus(@PathVariable("userid") final String userId) {
        return accountService.getAccountForUserId(userId)
                .map(account -> ResponseEntity.<Object>ok(toWireType(account)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(""));
    }

    private AccountWireType toWireType(final Account account) {
        return new AccountWireType(
                account.getId(),
                account.getStatus()
        );
    }

}
