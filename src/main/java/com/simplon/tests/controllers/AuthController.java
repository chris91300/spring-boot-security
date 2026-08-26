package com.simplon.tests.controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simplon.tests.entities.UserEntity;
import com.simplon.tests.repositories.UserRepository;
import com.simplon.tests.services.TokenService;
import com.simplon.tests.services.UserService;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final TokenService tokenService;

    public AuthController(
            UserRepository userRepositoryInjected,
            AuthenticationManager authManagerInjected,
            PasswordEncoder passwordEncoderInjected,
            TokenService tokenServiceInjected, UserService userService) {
        this.authManager = authManagerInjected;
        this.tokenService = tokenServiceInjected;
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserEntity registerUser(@RequestBody UserEntity user) throws Exception {
        return this.userService.create(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserEntity user) {

        Authentication auth = this.authManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword()));
        String token = tokenService.generateToken(auth);

        return token;
    }

}