package com.example.SocialSync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SocialSync.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public long countOnlyUsers() {
        return userRepository.countByRole("ROLE_USER");
    }
}
