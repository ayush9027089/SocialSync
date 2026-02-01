package com.example.SocialSync.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "linkedin_scheduled_posts")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class LinkedInScheduledPost {
    @Id
    private String id;
    private String userId; // ✅ Changed from LinkedInAccount DBRef to simple String
    private String content;
    private String mediaPath; 
    private LocalDateTime scheduledAt;
    private LocalDateTime postedAt;
    private PostStatus status;
    private String failureReason;
    private String platformPostUrn;

    @jakarta.persistence.PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}