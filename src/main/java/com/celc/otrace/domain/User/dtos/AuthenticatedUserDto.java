package com.celc.otrace.domain.User.dtos;

import com.celc.otrace.domain.User.User;

public record AuthenticatedUserDto(
    String email,
    String name,
    String token
) {

    public AuthenticatedUserDto(User user, String token) {
        this(user.getEmail(), user.getName(), token);
    }
    
}
