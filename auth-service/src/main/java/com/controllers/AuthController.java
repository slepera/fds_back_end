package com.controllers;

import com.entities.JWTToken;
import com.entities.LoginRequest;
import com.entities.User;
import com.entities.value_objects.UserVO;
import com.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<JWTToken> register(@RequestBody User authRequest) {
        return ResponseEntity.ok(authService.register(authRequest));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<UserVO> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

}
