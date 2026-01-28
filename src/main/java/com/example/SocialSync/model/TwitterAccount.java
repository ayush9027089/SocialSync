package com.example.SocialSync.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "twitter_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TwitterAccount {
    @Id
    private String id;
    private String twitterUserId;
    private String twitterScreenName;
    private String accessToken;
    private String refreshToken; // Twitter V2 needs refresh tokens
    private LocalDateTime connectedAt;

    @DBRef(lazy = true)
    private User user;

    @jakarta.persistence.PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}
