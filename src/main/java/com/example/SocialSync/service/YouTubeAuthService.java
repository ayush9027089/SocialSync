package com.example.SocialSync.service;

import com.example.SocialSync.config.YouTubeConfig;
import com.example.SocialSync.model.User;
import com.example.SocialSync.model.YouTubeAccount;
import com.example.SocialSync.repository.UserRepository;
import com.example.SocialSync.repository.YouTubeAccountRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class YouTubeAuthService {

    private final YouTubeConfig config;
    private final YouTubeAccountRepository youtubeAccountRepository;
    private final UserRepository userRepository;

    /**
     * Step 1: Generate Google OAuth URL
     */
    public String getAuthorizationUrl() {

        return "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + config.getClientId()
                + "&redirect_uri=" + config.getRedirectUri()
                + "&response_type=code"
                + "&access_type=offline"
                + "&prompt=consent"
                + "&scope="
                + "https://www.googleapis.com/auth/youtube.upload "
                + "https://www.googleapis.com/auth/youtube.readonly "
                + "https://www.googleapis.com/auth/yt-analytics.readonly";
    }

    /**
     * Step 2: Handle OAuth callback
     */
    public void handleCallback(String code, String email) throws Exception {

        // Fetch logged-in user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Exchange code for access token
        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        GsonFactory.getDefaultInstance(),
                        "https://oauth2.googleapis.com/token",
                        config.getClientId(),
                        config.getClientSecret(),
                        code,
                        config.getRedirectUri()
                ).execute();

        // Fetch or create YouTube account
        YouTubeAccount account = youtubeAccountRepository.findByUserId(user.getId()).orElse(new YouTubeAccount());

        account.setUser(user);
        account.setAccessToken(tokenResponse.getAccessToken());
        account.setRefreshToken(tokenResponse.getRefreshToken());
        account.setTokenExpiry(
                LocalDateTime.now().plusSeconds(tokenResponse.getExpiresInSeconds())
        );

        youtubeAccountRepository.save(account);
    }
}
