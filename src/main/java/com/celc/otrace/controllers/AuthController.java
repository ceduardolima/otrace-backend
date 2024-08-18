package com.celc.otrace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.celc.otrace.domain.User.dtos.LoginDto;
import com.celc.otrace.domain.User.dtos.RegisterAccountDto;
import com.celc.otrace.services.AuthService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    @Autowired
    private final AuthService authService;

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<String> register(@RequestBody @Valid RegisterAccountDto data) {
        authService.register(data);
        return ResponseEntity.ok("Account was register successfuly");
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity login(@RequestBody @Valid LoginDto data) {
        authService.login(data);
        return ResponseEntity.ok().build();
    }
}
