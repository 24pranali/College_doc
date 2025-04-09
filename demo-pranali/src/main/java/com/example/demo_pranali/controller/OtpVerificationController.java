package com.example.demo_pranali.controller;

import com.example.demo_pranali.Model.VerifiedStudents;
import com.example.demo_pranali.repository.VerifiedStudentsRepository;
import com.example.demo_pranali.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/verify")
@CrossOrigin("http://localhost:5173")
public class OtpVerificationController {

    @Autowired
    private VerifiedStudentsRepository verifiedStudentsRepository;

    @Autowired
    private EmailService emailService;

    // store OTPs (we can replace with DB or Redis for production)
    private Map<String, String> otpStorage = new HashMap<>();

    // Send OTP if PRN and Email match
    @PostMapping("/send-otp")
    public Map<String, String> sendOtp(@RequestParam String prnNo, @RequestParam String email) {
        Optional<VerifiedStudents> verifiedStudent = verifiedStudentsRepository.findByPrnNoAndOfficialEmail(prnNo, email);
        Map<String, String> response = new HashMap<>();

        if (verifiedStudent.isPresent()) {
            String otp = generateOtp();
            otpStorage.put(email, otp);
            emailService.sendOtpEmail(email, otp);
            response.put("message", "OTP sent successfully to your official email.");
        } else {
            response.put("error", "PRN and email do not match any verified student.");
        }
        return response;
    }

    // Verify OTP
    @PostMapping("/verify-otp")
    public Map<String, String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        Map<String, String> response = new HashMap<>();

        if (otpStorage.containsKey(email) && otpStorage.get(email).equals(otp)) {
            otpStorage.remove(email); // Remove after successful verification
            response.put("message", "OTP verified successfully. Proceed to create account.");
        } else {
            response.put("error", "Invalid or expired OTP.");
        }
        return response;
    }

    // OTP Generator
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit
        return String.valueOf(otp);
    }
}
