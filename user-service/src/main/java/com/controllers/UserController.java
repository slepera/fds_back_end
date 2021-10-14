package com.controllers;

import com.entities.LoginRequest;
import com.entities.User;
import com.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping(value = "/login")
    public User login(@RequestBody LoginRequest user) {
        return userService.login(user);
    }

    @GetMapping(value = "/secure")
    public String getSecure() {
        return "Secure endpoint available";
    }

    @GetMapping(value = "/all")
    public List<User> getAll() {
        return userService.getAll() ;
    }
}
