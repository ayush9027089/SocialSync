package com.example.SocialSync.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.SocialSync.model.TwitterPost;

import java.time.LocalDateTime;
import java.util.List;
public interface TwitterPostRepository
extends MongoRepository<TwitterPost, String> {
List<TwitterPost> findByStatusAndScheduledTimeBefore(
String status,
LocalDateTime time
);
}
