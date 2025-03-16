package com.example.demo_pranali.controller;


import com.example.demo_pranali.Model.CreateAccount;
import com.example.demo_pranali.repository.CreateAccountRepository;
import com.example.demo_pranali.service.CreateAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/students")
@CrossOrigin("http://localhost:5173")
public class CreateAccountController
{
    @Autowired
    private CreateAccountService createAccountService;

    // Create Account API (POST)
    @PostMapping("/create")
    public CreateAccount createStudent(@RequestBody CreateAccount student)
    {
        return createAccountService.saveStudent(student);
    }

    // Fetch Student Details by Roll No. (GET)
    @GetMapping("/{rollNo}")
    public Optional<CreateAccount> getStudentByRollNo(@PathVariable String rollNo) {
        return createAccountService.getStudentByRollNo(rollNo);
    }


}
