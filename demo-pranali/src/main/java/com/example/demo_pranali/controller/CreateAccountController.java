package com.example.demo_pranali.controller;


import com.example.demo_pranali.Model.CreateAccount;
import com.example.demo_pranali.Model.VerifiedStudents;
import com.example.demo_pranali.repository.CreateAccountRepository;
import com.example.demo_pranali.repository.VerifiedStudentsRepository;
import com.example.demo_pranali.service.CreateAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/students")
@CrossOrigin("http://localhost:5173")
public class CreateAccountController
{
    @Autowired
    private CreateAccountService createAccountService;

    @Autowired
    private VerifiedStudentsRepository verifiedStudentsRepository;

    // Create Account API (POST)
//    @PostMapping("/create")
//    public CreateAccount createStudent(@RequestBody CreateAccount student)
//    {
//        return createAccountService.saveStudent(student);
//    }

    @PostMapping("/create")
    public ResponseEntity<?> createStudent(@RequestBody CreateAccount student) {
        // Check if PRN and Email exist in verified_students table
        Optional<VerifiedStudents> verified = verifiedStudentsRepository.findByPrnNoAndOfficialEmail(
                student.getPrnNo(), student.getEmail()
        );

        if (verified.isPresent()) {
            // Save student to create_account table
            CreateAccount savedStudent = createAccountService.saveStudent(student);
            return ResponseEntity.ok(savedStudent);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You are not verified to create an account. Please use your official PRN and Email.");
        }
    }


    // Fetch Student Details by Roll No. (GET)
    @GetMapping("/{rollNo}")
    public Optional<CreateAccount> getStudentByRollNo(@PathVariable String rollNo) {
        return createAccountService.getStudentByRollNo(rollNo);
    }
    @GetMapping("/email/{email}")
    public Optional<CreateAccount> getStudentByEmail(@PathVariable String email) {
        return createAccountService.getStudentByEmail(email);
    }

}
