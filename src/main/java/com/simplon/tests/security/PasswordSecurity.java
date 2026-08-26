package com.simplon.tests.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordSecurity {
    private final BCryptPasswordEncoder encoder;

    public PasswordSecurity(BCryptPasswordEncoder encoderInjected) {
        this.encoder = encoderInjected;
    }

    public String hashpassword(String password) {
        return encoder.encode(password);
    }

    public boolean verifyPassword(String password, String passwordHashed) {
        return encoder.matches(password, passwordHashed);
    }

}
