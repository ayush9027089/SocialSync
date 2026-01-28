package com.example.SocialSync.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.SocialSync.model.LinkedInScheduledPost;
import com.example.SocialSync.model.PostStatus;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface LinkedInScheduledPostRepository extends MongoRepository<LinkedInScheduledPost, String> {
List<LinkedInScheduledPost> findByStatusAndScheduledAtLessThanEqual(PostStatus status, LocalDateTime time );
}