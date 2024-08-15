package com.celc.otrace.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.celc.otrace.domain.User.Account;
import com.celc.otrace.domain.User.User;
import com.celc.otrace.domain.User.dtos.RegisterAccountDto;
import com.celc.otrace.repositories.AccountRepository;
import com.celc.otrace.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
    @Autowired
    private final AccountRepository accountRepository;
    @Autowired
    private final UserRepository userRepository;

    public void register(RegisterAccountDto data) {
        var savedAccount = accountRepository.save(new Account(data));
        var user = new User(data, savedAccount);
        userRepository.save(user);
    }

}
