package com.example.demo_pranali.repository;

import com.example.demo_pranali.Model.CreateAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreateAccountRepository extends JpaRepository<CreateAccount , Long>
{
//    CreateAccount findByRollNo(String rollNo); // Fetch student by Roll No
Optional<CreateAccount> findByEmail(String email);
    Optional<CreateAccount> findByRollNo(String rollNo);

}
