package com.example.SocialSync.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.SocialSync.model.LinkedInAccount;
import com.example.SocialSync.model.User;

import java.util.Optional;
@Repository
public interface LinkedInAccountRepository extends MongoRepository<LinkedInAccount, String> {
Optional<LinkedInAccount> findByUser(User user);
}