package com.example.SocialSync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponse {
    private String message;
    private String email;
    private String token; // The JWT for auto-login
    private boolean success;
}
