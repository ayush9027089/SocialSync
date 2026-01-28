package com.example.SocialSync.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.SocialSync.model.PostStatus;
import com.example.SocialSync.model.YouTubeScheduledPost;

import java.time.LocalDateTime;
import java.util.List;
public interface YouTubeScheduledPostRepository extends MongoRepository<YouTubeScheduledPost, String> {
List<YouTubeScheduledPost> findByPlatformAndStatusAndScheduledAtLessThanEqual(String platform, PostStatus status, LocalDateTime time );
}