package com.celc.otrace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.celc.otrace.domain.User.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
