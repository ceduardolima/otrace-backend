package com.celc.otrace.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.celc.otrace.domain.User.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccountEmail(String email);
}
