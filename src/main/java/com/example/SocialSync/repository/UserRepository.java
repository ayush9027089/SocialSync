package com.example.SocialSync.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.SocialSync.model.User;

import java.util.Optional;
public interface UserRepository extends MongoRepository<User, String> {
Optional <User> findByEmail(String email);
Optional<User> findByResetToken(String resetToken);
boolean existsByEmail(String email);

long countByRole(String role);
}