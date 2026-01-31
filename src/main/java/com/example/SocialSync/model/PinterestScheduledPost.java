package com.example.SocialSync.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;
@Document(collection = "pinterest_scheduled_posts")
@Data
public class PinterestScheduledPost {
@Id
private String id;
private String title;
private String userId;
private String description;
private String boardId;
private String imageUrl;
private String destinationLink;
private LocalDateTime scheduledTime;
private String status; 
private String errorMessage;
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBoardId() { return boardId; }
    public void setBoardId(String boardId) { this.boardId = boardId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDestinationLink() { return destinationLink; }
    public void setDestinationLink(String destinationLink) { this.destinationLink = destinationLink; }

    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }
}