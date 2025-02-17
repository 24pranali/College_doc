package com.example.demo_pranali.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  // Marks this class as a configuration class for Spring Security
public class SecurityConfig {

    // Bean to encode and decode passwords using BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        // Disable CSRF for API (since it's not needed in typical API scenarios)
        http.csrf(csrf -> csrf.disable())

                // Define URL access rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/signup", "/auth/login").permitAll()  // Allow signup and login without authentication
                        .anyRequest().authenticated()  // Require authentication for other requests
                )

                // Disable form-based login (we're using API-based authentication)
                .formLogin(login -> login.disable())

                // Disable HTTP Basic Authentication
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }
}
