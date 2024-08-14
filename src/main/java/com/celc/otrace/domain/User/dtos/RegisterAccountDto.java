package com.celc.otrace.domain.User.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterAccountDto(
    @NotBlank
    @Email
    String email,
    @NotBlank
    String name,
    @NotBlank
    String password
) {

}
