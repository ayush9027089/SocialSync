package com.example.SocialSync.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.SocialSync.model.LinkedInAccount;
import com.example.SocialSync.model.User;
import com.example.SocialSync.repository.LinkedInAccountRepository;
import com.example.SocialSync.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LinkedInAuthService {

    @Value("${linkedin.client-id}")
    private String clientId;

    @Value("${linkedin.client-secret}")
    private String clientSecret;

    @Value("${linkedin.redirect-uri}")
    private String redirectUri;

    private final LinkedInAccountRepository accountRepository;
    private final UserRepository userRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Step 1: Generate LinkedIn Authorization URL
     */
    public String getAuthorizationUrl(String userId) {

        return "https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id=" + clientId
                + "&redirect_uri=" + redirectUri + "&state=" + userId +"&scope=openid%20profile%20email%20w_member_social";
    }

    /**
     * Step 2: Handle OAuth Callback
     */
    public void handleCallback(String code, String userId) {

        // Exchange code → access token
        String accessToken = exchangeCodeForToken(code);

        // Fetch LinkedIn user id
        String linkedinUserId = fetchLinkedInUserId(accessToken);

        // Fetch logged-in user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Save LinkedIn account
        LinkedInAccount account = LinkedInAccount.builder()
                .accessToken(accessToken)
                .linkedinUserId(linkedinUserId)
                .connectedAt(LocalDateTime.now())
                .user(user)
                .build();

        accountRepository.save(account);
    }

    /**
     * Exchange authorization code for access token
     */
    private String exchangeCodeForToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> body = Map.of(
                "grant_type", "authorization_code",
                "code", code,
                "redirect_uri", redirectUri,
                "client_id", clientId,
                "client_secret", clientSecret
        );

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                        "https://www.linkedin.com/oauth/v2/accessToken",
                        entity,
                        String.class
                );
        try {
            JsonNode json = objectMapper.readTree(response.getBody());
            return json.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse access token", e);
        }
    }

    /**
     * Fetch LinkedIn user id (OpenID profile)
     */
    private String fetchLinkedInUserId(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                        "https://api.linkedin.com/v2/userinfo",
                        HttpMethod.GET,
                        entity,
                        String.class
                );
        try {
            JsonNode json = objectMapper.readTree(response.getBody());
            return json.get("sub").asText(); // LinkedIn user id
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch LinkedIn user id", e);
        }
    }
}

