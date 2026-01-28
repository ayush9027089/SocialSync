package com.example.SocialSync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PinterestPinDTO {
    private String title;
    private String description;
    private String boardId;
    private String imageUrl;
    private String destinationLink;
    private LocalDateTime scheduledTime; // User kab post karna chahta hai
// Manually added Getters and Setters
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