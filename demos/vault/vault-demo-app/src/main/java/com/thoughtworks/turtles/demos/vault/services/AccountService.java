package com.thoughtworks.turtles.demos.vault.services;

import com.thoughtworks.turtles.demos.vault.domain.Account;
import com.thoughtworks.turtles.demos.vault.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccountForUserId(final String userId) {
        return accountRepository.findAccountByUserId(userId);
    }
}
