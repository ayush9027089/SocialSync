package com.example.SocialSync.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "whatsapp_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WhatsAppAccount {
    @Id
    private String id;
    private String facebookUserId;
    private String accessToken; // Long-lived token
    private String wabaId;      // WhatsApp Business Account ID
    private String phoneNumberId; // The specific phone number ID to send from
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