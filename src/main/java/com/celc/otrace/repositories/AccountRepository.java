package com.celc.otrace.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.celc.otrace.domain.User.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{
    Optional<Account> findByEmail(String email);
}
