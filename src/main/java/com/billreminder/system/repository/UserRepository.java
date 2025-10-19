package com.billreminder.system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billreminder.system.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // Custom query method: Find a user by their email (unique column)
    Optional<User> findByEmail(String email);
}