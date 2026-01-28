package com.example.SocialSync.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
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
private String content;
private String mediaPath; // image/video file path
private LocalDateTime scheduledAt;
private LocalDateTime postedAt;
private PostStatus status;
private String failureReason;
private String platformPostUrn;
@DBRef(lazy = true)
private LinkedInAccount account;
@jakarta.persistence.PrePersist
public void prePersist() {
if (id == null) {
id = UUID.randomUUID().toString();
}
}
}