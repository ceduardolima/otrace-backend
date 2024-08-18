package com.celc.otrace.services;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import com.celc.otrace.domain.User.dtos.LoginDto;
import com.celc.otrace.domain.User.dtos.RegisterAccountDto;
import com.celc.otrace.repositories.AccountRepository;
import com.celc.otrace.repositories.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integrationtest")
@TestInstance(Lifecycle.PER_CLASS)
public class AuthServiceTest {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    private final String DEFAULT_PASSWORD = "123456";

    @Autowired
    public AuthServiceTest(AuthService authService, UserRepository userRepository,
            AccountRepository accountRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void registerValidAccount() {
        var dto = createRegisterAccountDto("valid@email.com");
        var user = authService.register(dto);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getName()).isEqualTo(dto.name());
        assertThat(user.getAccount().getEmail()).isEqualTo(dto.email());
    }

    @Test
    void registerInvalidAccount() {
        registerDefaultAccount();
        var dto = createRegisterAccountDto("email@email.com");
        var exception = assertThrows(ResponseStatusException.class, () -> {
            authService.register(dto);
        });
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void loginAccount() {
        registerDefaultAccount();
        var loginDto = createLoginDto("email@email.com");
        var user = authService.login(loginDto);
        assertThat(user.getAccount().getEmail()).isEqualTo(loginDto.email());
        assertThat(user.getAccount().getPassword()).isNotEqualTo(loginDto.password());
    }

    @Test
    void loginWithInvalidAccount() {
        var invalidLoginDto = createLoginDto("not-existing@email.com");
        var exception = assertThrows(ResponseStatusException.class, () -> {
            authService.login(invalidLoginDto);
        });
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    void registerDefaultAccount() {
        var registerDto = createRegisterAccountDto("email@email.com");
        authService.register(registerDto);
    }

    RegisterAccountDto createRegisterAccountDto(String email) {
        return new RegisterAccountDto(email, "name", DEFAULT_PASSWORD);
    }

    LoginDto createLoginDto(String email) {
        return new LoginDto(email, DEFAULT_PASSWORD);
    }
}
