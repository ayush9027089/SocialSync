package com.example.SocialSync.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.SocialSync.model.PinterestScheduledPost;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface PinterestScheduledPostRepository extends MongoRepository<PinterestScheduledPost, String> {
// Fix: Ye method waisa hi hona chahiye jaisa scheduler mein hai
List<PinterestScheduledPost> findByStatusAndScheduledTimeBefore(String status, LocalDateTime time);
}