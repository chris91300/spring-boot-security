package com.simplon.tests.services;

import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.simplon.tests.entities.UserEntity;
import com.simplon.tests.repositories.UserRepository;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public UserService(UserRepository userRepositoryInjected, PasswordEncoder passwordEncoder){
        this.userRepository = userRepositoryInjected;
        this.passwordEncoder = passwordEncoder;
    }


    public UserEntity create(UserEntity user) throws Exception{

        Optional<UserEntity> userFound = this.userRepository.findByEmail(user.getEmail());
        if(userFound.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette email existe déjà");
        }
         user.setPassword(passwordEncoder.encode(user.getPassword()));
         return userRepository.save(user);
    }
}
