package com.example.SocialSync.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "youtube_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YouTubeAccount {
@Id
private String id;
private String channelId;
private String channelName;
private String accessToken;
private String refreshToken;
private LocalDateTime tokenExpiry;
@DBRef(lazy = true)
private User user;
@jakarta.persistence.PrePersist
public void prePersist() {
if (id == null) {
id = UUID.randomUUID().toString();
}
}
}