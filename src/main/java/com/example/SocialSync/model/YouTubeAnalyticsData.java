package com.example.SocialSync.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;
@Document(collection = "youtube_analytics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YouTubeAnalyticsData {
@Id
private String id;
private String channelId;
private LocalDate analyticsDate;
private Long views;
private Long likes;
private Long comments;
private Long subscribersGained;
private Long subscribersLost;
@jakarta.persistence.PrePersist
public void prePersist() {
if (id == null) {
id = UUID.randomUUID().toString();
}
}
}