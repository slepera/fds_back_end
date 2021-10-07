package com.controllers;

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
        return userService.save(user);
    }

    @PostMapping(value = "/login")
    public User login(@RequestBody User user) {
        return userService.authenticate(user);
    }

    @PutMapping(value = "/sftp_put/{path}")
    public String sftp_put(@PathVariable String path) {
        return userService.putSftp(path);
    }

    @GetMapping(value = "/sftp_get/{path}")
    public String sftp_get(@PathVariable String path) {
        return userService.getSftp(path);
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
