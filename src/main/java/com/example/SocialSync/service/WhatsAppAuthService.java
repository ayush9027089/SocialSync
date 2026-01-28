package com.example.SocialSync.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.SocialSync.model.User;
import com.example.SocialSync.model.WhatsAppAccount;
import com.example.SocialSync.repository.UserRepository;
import com.example.SocialSync.repository.WhatsAppAccountRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WhatsAppAuthService {

    // NOTE: WhatsApp uses Facebook Apps for Login
    @Value("${facebook.client-id}") 
    private String clientId;

    @Value("${facebook.client-secret}")
    private String clientSecret;

    @Value("${facebook.redirect-uri}")
    private String redirectUri;

    private final WhatsAppAccountRepository accountRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getAuthorizationUrl(String userId) {
        // Permissions: whatsapp_business_management, whatsapp_business_messaging
        return "https://www.facebook.com/v18.0/dialog/oauth?" +
                "client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&state=" + userId +
                "&scope=whatsapp_business_management,whatsapp_business_messaging";
    }

    public void handleCallback(String code, String userId) {
        // 1. Exchange Code for Token
        String accessToken = exchangeCodeForToken(code);

        // 2. Find User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Save/Update WhatsApp Account
        WhatsAppAccount account = accountRepository.findByUser(user)
                .orElse(new WhatsAppAccount());
        
        account.setAccessToken(accessToken);
        account.setUser(user);
        account.setConnectedAt(LocalDateTime.now());
        
        // TODO: ideally here you fetch the WABA ID and Phone ID from Meta API
        // For now, we save the token so the connection is "Green"
        
        accountRepository.save(account);
    }

    private String exchangeCodeForToken(String code) {
        String url = "https://graph.facebook.com/v18.0/oauth/access_token?" +
                "client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&client_secret=" + clientSecret +
                "&code=" + code;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JsonNode json = objectMapper.readTree(response.getBody());
            return json.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to exchange Facebook token: " + e.getMessage());
        }
    }
}