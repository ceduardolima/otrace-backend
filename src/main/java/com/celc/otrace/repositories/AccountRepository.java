package com.celc.otrace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.celc.otrace.domain.User.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{

}
