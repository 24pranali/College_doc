package com.example.demo_pranali.Model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "auth_users")
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;


    public AuthUser(Long id, String name, String email, String password, Role role)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Enumerated(EnumType.STRING)
    private Role role;

    public AuthUser() {

        this.role = Role.STUDENT ; //default role is student
    }
    @JsonCreator
    public AuthUser(
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("role") Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }




    // Getters
    public Role getRole() {

        return role;
    }

    @JsonIgnore // Prevent password from being included in API response
    public String getPassword() { return password; }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    // Setters


    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;  // Don't encode here, encode only in AuthUserService
    }

    public void setRole(Role role) {
        this.role = role;
    }


    public enum Role {
        STUDENT, ADMIN
    }




}