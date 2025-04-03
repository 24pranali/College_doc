package com.example.demo_pranali.controller;

import com.example.demo_pranali.Model.User;
import com.example.demo_pranali.exception.USerNotFoundException;
import com.example.demo_pranali.repository.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;


@RestController
@CrossOrigin("http://localhost:5173")
public class UserController
{
    @Autowired
    private UserRespository userRespository;

    //sending data into database
    @PostMapping("/user")
    User newUser(@RequestBody User newUser)
    {
        return userRespository.save(newUser);

    }

    //getting the data from database
    @GetMapping("/users")
    List<User> getAllUser() // List which is used bcz return no of user
    {
        return userRespository.findAll(); // which return the all user which is stored in database
    }


    @GetMapping("/user/{id}")
    User getUserById(@PathVariable Long id) throws UserPrincipalNotFoundException //specific with user id
    {
        return userRespository.findById(id)
                .orElseThrow(() -> new UserPrincipalNotFoundException(id.toString())); // Convert Long to String
    }

    @PutMapping("/user/{id}")
    User updateUser(@RequestBody User newUser, @PathVariable Long id) {
        return userRespository.findById(id)
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    return userRespository.save(user);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
    }

    @DeleteMapping("/users/{id}")
    String deleteUser (@PathVariable Long id)
    {
        if(!userRespository.existsById(id))
        {
            throw new USerNotFoundException(id);
        }
        userRespository.deleteById(id);
        return "User with id "+ id+ " has been deleted Success";

    }



}






