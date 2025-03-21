package com.example.demo_pranali.service;

import com.example.demo_pranali.Model.CreateAccount;
import com.example.demo_pranali.repository.CreateAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreateAccountService
{
    @Autowired

    private CreateAccountRepository repository;

<<<<<<< HEAD
=======
    public Optional<CreateAccount> getStudentByEmail(String email) {
        return repository.findByEmail(email);
    }
    // Save student details
>>>>>>> 648ed6c5f969d2a9cee6ad4c05515acd06136142
    public CreateAccount saveStudent(CreateAccount student)
    {
        return repository.save(student);
    }

    // Fetch student by Roll No.
    public Optional<CreateAccount> getStudentByRollNo(String rollNo)
    {
        return repository.findByRollNo(rollNo);
    }


}
