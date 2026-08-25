package com.simplon.tests.services;

import org.springframework.stereotype.Service;

import com.simplon.tests.repositories.UserRepository;

@Service
public class UserService {
    
    private UserRepository userRepository;

    public UserService(UserRepository userRepositoryInjected){
        this.userRepository = userRepositoryInjected;
    }

    
}
