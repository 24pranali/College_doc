package com.example.demo_pranali.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Disable CSRF (for testing purposes)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // ✅ Allow ALL requests without authentication
                )
                .formLogin(login -> login.disable())  // Disable Spring Security Login Page
                .httpBasic(basic -> basic.disable()); // Disable HTTP Basic Authentication

        System.out.println("🔐 SecurityConfig: All Requests Permitted! 🔐");
        return http.build();
    }
}
