package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.DCUser;

public interface UserRepository extends JpaRepository<DCUser, Long> {
    Optional<DCUser> findByUsername(String username);
}
