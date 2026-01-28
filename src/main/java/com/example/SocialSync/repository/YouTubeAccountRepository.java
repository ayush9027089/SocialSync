package com.example.SocialSync.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.SocialSync.model.YouTubeAccount;

import java.util.Optional;
public interface YouTubeAccountRepository extends MongoRepository<YouTubeAccount, String> {
Optional<YouTubeAccount> findByUserId(String userId);
}