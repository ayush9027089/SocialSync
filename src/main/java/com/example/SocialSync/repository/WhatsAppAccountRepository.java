package com.example.SocialSync.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.SocialSync.model.User;
import com.example.SocialSync.model.WhatsAppAccount;

import java.util.Optional;

public interface WhatsAppAccountRepository extends MongoRepository<WhatsAppAccount, String> {
    Optional<WhatsAppAccount> findByUser(User user);
}