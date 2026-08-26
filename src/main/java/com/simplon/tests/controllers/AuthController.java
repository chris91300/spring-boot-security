package com.simplon.tests.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simplon.tests.entities.UserEntity;
import com.simplon.tests.services.UserService;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(
            PasswordEncoder passwordEncoderInjected,
            UserService userServiceInjected) {
        this.userService = userServiceInjected;
    }

    @PostMapping("/register")
    public UserEntity registerUser(@RequestBody UserEntity user) throws Exception {

        return this.userService.create(user);
    }
}