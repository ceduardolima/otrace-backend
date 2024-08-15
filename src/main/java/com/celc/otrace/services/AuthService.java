package com.celc.otrace.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.celc.otrace.domain.User.Account;
import com.celc.otrace.domain.User.User;
import com.celc.otrace.domain.User.dtos.LoginDto;
import com.celc.otrace.domain.User.dtos.RegisterAccountDto;
import com.celc.otrace.repositories.AccountRepository;
import com.celc.otrace.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private PasswordEncoder encoder;

    public void register(RegisterAccountDto data) {
        var savedAccount = accountRepository.save(new Account(null, data.email(), encoder.encode(data.password())));
        var user = new User(data, savedAccount);
        userRepository.save(user);
    }

    public User login(LoginDto data) {
        var user = userRepository.findByAccountEmail(data.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid email or password"));
        boolean passwordMatch = encoder.matches(data.password(), user.getAccount().getPassword());
        if (!passwordMatch) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid email or password");
        return user;
    }
}
