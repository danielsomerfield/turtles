package com.thoughtworks.turtles.demos.blackbox.services;

import com.thoughtworks.turtles.demos.blackbox.domain.Account;
import com.thoughtworks.turtles.demos.blackbox.domain.User;
import com.thoughtworks.turtles.demos.blackbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final UserRepository userRepository;

    @Autowired
    public AccountService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<Account> getAccountForUserId(final String userId) {
        return userRepository.findUserById(userId).flatMap(User::getAccount);
    }

    public String registerUser(final Account account) {
        return this.userRepository.insertUser(account);
    }

}
