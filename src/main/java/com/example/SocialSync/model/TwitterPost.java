package com.example.SocialSync.model;

import lombok.Data; // Use Lombok to save space if you have it
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "twitter_posts")
@Data // Adds Getters/Setters automatically
public class TwitterPost {
    @Id
    private String id;
    private String tweetId;
    private String content;
    private String status; // PENDING, POSTED, FAILED
    private LocalDateTime scheduledTime;
    private LocalDateTime postedAt;
    
    // ✅ NEW: Link the post to the specific Twitter Account
    @DBRef
    private TwitterAccount account; 

    @jakarta.persistence.PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        
    }
}