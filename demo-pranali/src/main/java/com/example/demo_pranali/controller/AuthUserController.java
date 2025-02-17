package com.example.demo_pranali.controller;

import com.example.demo_pranali.Model.AuthUser;
import com.example.demo_pranali.service.AuthUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth") // base path for all API
public class AuthUserController {

    private final AuthUserService authUserService;

    public AuthUserController(AuthUserService authUserService)
    {
        this.authUserService = authUserService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody AuthUser user)
    {
        try {
            AuthUser newUser = authUserService.registerUser(user);
            return ResponseEntity.ok(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String password)
    {
        Optional<AuthUser> user = authUserService.findByEmail(email);
        System.out.println("sdsdsdsdsd"+email+" - "+password+"jjj"+user.isPresent()+"hhhhh"+user.get().getPassword());
        if (user.isPresent() && authUserService.verifyPassword(password, user.get().getPassword()))
        {
            // If login is successful, return a success message or a token
            return ResponseEntity.ok("Login successful:)");
        }
        else
        {
            return ResponseEntity.status(401).body("Invalid email or password :(");
        }
    }
}
