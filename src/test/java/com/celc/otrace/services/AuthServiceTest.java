package com.celc.otrace.services;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    public AuthServiceTest(AuthService authService, UserRepository userRepository, AccountRepository accountRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @AfterAll
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

    void registerDefaultAccount() {
        var registerDto = createRegisterAccountDto("email@email.com");
        authService.register(registerDto);
    }


    RegisterAccountDto createRegisterAccountDto(String email) {
        return new RegisterAccountDto(email, "name", "123456");
    }
}
