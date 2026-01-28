package com.example.SocialSync.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.SocialSync.model.TwitterAccount;
import com.example.SocialSync.model.User;

import java.util.Optional;

public interface TwitterAccountRepository extends MongoRepository<TwitterAccount, String>{
    Optional<TwitterAccount> findByUser(User user);
}   
