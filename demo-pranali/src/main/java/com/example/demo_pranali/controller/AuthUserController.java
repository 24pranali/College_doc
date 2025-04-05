package com.example.demo_pranali.controller;
import com.example.demo_pranali.service.EmailService;
import com.example.demo_pranali.Model.AuthUser;
import com.example.demo_pranali.repository.AuthUserRepository;
import com.example.demo_pranali.service.AuthUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth") // base path for all API
@CrossOrigin("http://localhost:5173")
public class AuthUserController
{

    private final AuthUserService authUserService;
    private final AuthUserRepository authUserRepository;
    private final EmailService emailService;
    private final Map<String, String> otpStorage = new HashMap<>();
    public AuthUserController(AuthUserService authUserService, AuthUserRepository authUserRepository,EmailService emailService)
    {
        this.authUserService = authUserService;
        this.authUserRepository = authUserRepository;

        this.emailService = emailService;
    }



    /**
     * Step 2: Verify OTP and Complete Registration
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtpAndRegister(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String name = request.get("name");
        String password = request.get("password");

        // Check if OTP is valid
        String storedOtp = otpStorage.get(email);
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStorage.remove(email); // Remove OTP after successful verification

            // Create new user
            AuthUser newUser = new AuthUser();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPassword(password); // Hashing is done inside AuthUserService
            newUser.setRole(AuthUser.Role.STUDENT); // Default role is student

            authUserService.registerUser(newUser);
            return ResponseEntity.ok("✅ Registration successful!");
        }

        return ResponseEntity.status(400).body("❌ Invalid OTP!");
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        // Check if email is provided
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required!");
        }

        // Check if the email already exists in the database
        Optional<AuthUser> existingUser = authUserRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("User with this email already exists!");
        }

        // Generate a 6-digit OTP
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStorage.put(email, otp);

        // Send OTP to email
        String subject = "Your OTP for Registration";
        String message = "Hello,\n\nYour OTP for registration is: " + otp + "\n\nPlease enter this to complete registration.";
        emailService.sendEmail(email, subject, message);

        return ResponseEntity.ok("✅ OTP sent to " + email + ". Please enter OTP to complete registration.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> request) {
        String identifier = request.get("identifier"); // Can be email or username
        String password = request.get("password");

        if (identifier == null || password == null) {
            return ResponseEntity.badRequest().body("❌ Email/Username and Password are required!");
        }

        // ✅ Hardcoded Admin Login
        if (identifier.equalsIgnoreCase("sanika") && password.equals("Sadmaashiq")) {
            List<AuthUser> students = authUserRepository.findAll();
            return ResponseEntity.ok(students); // Show all students to admin
        }

        // ✅ Find user by Email or Username
        Optional<AuthUser> user = authUserRepository.findByEmailOrName(identifier, identifier);

        if (user.isPresent() && authUserService.verifyPassword(password, user.get().getPassword())) {
            return ResponseEntity.ok("✅ Student Login Successful!");
        } else {
            return ResponseEntity.status(401).body("❌ Invalid Credentials!");
        }
    }
    @GetMapping("/auth_users")
    public ResponseEntity<List<AuthUser>> getAllStudents() {
        List<AuthUser> students = authUserRepository.findAll();
        return ResponseEntity.ok(students);
    }

}
