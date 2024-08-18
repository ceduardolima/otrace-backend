package com.celc.otrace.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.celc.otrace.domain.User.Account;
import com.celc.otrace.domain.User.User;
import com.celc.otrace.domain.User.dtos.LoginDto;
import com.celc.otrace.domain.User.dtos.RegisterAccountDto;
import com.celc.otrace.repositories.AccountRepository;
import com.celc.otrace.repositories.UserRepository;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private TokenService tokenService;

    public User register(RegisterAccountDto data) {
        var account = tryRegisterAccount(data);
        var user = new User(data, account);
        return userRepository.save(user);
    }

    private Account tryRegisterAccount(RegisterAccountDto data) {
        try {
            final var accountBuffer = Account.newAccount(data.email(), encoder.encode(data.password()));
            return accountRepository.save(accountBuffer);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
    }

    public User login(LoginDto data) {
        var user = userRepository.findByAccountEmail(data.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid email or password"));
        boolean passwordMatch = encoder.matches(data.password(), user.getAccount().getPassword());
        if (!passwordMatch)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid email or password");
        return user;
    }

    public String genToken(Account account) {
        return tokenService.genToken(account);
    }
}
