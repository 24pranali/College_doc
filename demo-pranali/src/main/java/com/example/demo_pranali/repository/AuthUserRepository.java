package com.example.demo_pranali.repository;

import com.example.demo_pranali.Model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// This interface interacts with the database
// No need to write SQL queries.
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findByEmail(String email); // Find user by email for login
}
