package com.example.SocialSync.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.SocialSync.model.ContactQuestion;

@Repository
public interface ContactQuestionRepository extends MongoRepository<ContactQuestion, String> {
    // You can add custom finders here if needed, e.g., findByEmail(String email)
}