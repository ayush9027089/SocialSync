package com.example.SocialSync.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "contact_questions")
public class ContactQuestion {
    @Id
    private String id;

    private String name;
    private String email;
    private String message;
    private String source; // e.g., "FAQ Page", "Dashboard"
    
    private LocalDateTime submittedAt;

    @jakarta.persistence.PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.submittedAt == null) {
            this.submittedAt = LocalDateTime.now();
        }
    }
}
