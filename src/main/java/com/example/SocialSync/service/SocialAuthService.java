package com.example.SocialSync.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SocialAuthService {

    private final SocialConnectionService connectionService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Inject all credentials
    @Value("${spring.security.oauth2.client.registration.google.client-id}") private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}") private String googleRedirectUri;
    
    @Value("${spring.security.oauth2.client.registration.facebook.client-id}") private String fbClientId;
    @Value("${spring.security.oauth2.client.registration.facebook.redirect-uri}") private String fbRedirectUri;

    @Value("${spring.security.oauth2.client.registration.linkedin.client-id}") private String liClientId;
    @Value("${spring.security.oauth2.client.registration.linkedin.client-secret}") private String liClientSecret;
    @Value("${spring.security.oauth2.client.registration.linkedin.redirect-uri}") private String liRedirectUri;

    @Value("${spring.security.oauth2.client.registration.pinterest.client-id}") private String pinClientId;
    @Value("${spring.security.oauth2.client.registration.pinterest.client-secret}") private String pinClientSecret;
    @Value("${spring.security.oauth2.client.registration.pinterest.redirect-uri}") private String pinRedirectUri;

    // 1. GENERATE AUTH URL
    public String getAuthorizationUrl(String platform) {
        switch (platform.toUpperCase()) {
            case "GOOGLE":
            case "YOUTUBE":
                return "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + googleClientId +
                        "&redirect_uri=" + googleRedirectUri +
                        "&response_type=code&scope=openid%20profile%20email%20https://www.googleapis.com/auth/youtube.upload&access_type=offline&prompt=consent";
            
            case "FACEBOOK":
                return "https://www.facebook.com/v12.0/dialog/oauth?client_id=" + fbClientId +
                        "&redirect_uri=" + fbRedirectUri + "&scope=public_profile,email";

            case "LINKEDIN":
                return "https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id=" + liClientId +
                        "&redirect_uri=" + liRedirectUri + "&scope=openid%20profile%20email%20w_member_social";

            case "PINTEREST":
                // Pinterest requires a unique state to prevent CSRF, but we use it for UserID mapping in the controller
                return "https://www.pinterest.com/oauth/?client_id=" + pinClientId +
                        "&redirect_uri=" + pinRedirectUri +
                        "&response_type=code&scope=boards:read,pins:read,pins:write,user_accounts:read";

            default:
                throw new RuntimeException("Platform not supported: " + platform);
        }
    }

    // 2. EXCHANGE CODE FOR TOKEN & SAVE
    public void handleCallback(String platform, String code, String userId) {
        String accessToken = null;
        String platformUserId = null;
        String username = null;

        try {
            if ("LINKEDIN".equalsIgnoreCase(platform)) {
                // A. Exchange Code
                String tokenUrl = "https://www.linkedin.com/oauth/v2/accessToken";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                
                MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
                body.add("grant_type", "authorization_code");
                body.add("code", code);
                body.add("redirect_uri", liRedirectUri);
                body.add("client_id", liClientId);
                body.add("client_secret", liClientSecret);

                ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, new HttpEntity<>(body, headers), Map.class);
                accessToken = (String) response.getBody().get("access_token");

                // B. Get User Info
                HttpHeaders profileHeaders = new HttpHeaders();
                profileHeaders.setBearerAuth(accessToken);
                ResponseEntity<Map> profile = restTemplate.exchange("https://api.linkedin.com/v2/userinfo", HttpMethod.GET, new HttpEntity<>(profileHeaders), Map.class);
                
                platformUserId = (String) profile.getBody().get("sub"); // 'sub' is the ID in OpenID
                username = (String) profile.getBody().get("name");

            } else if ("PINTEREST".equalsIgnoreCase(platform)) {
                // A. Exchange Code (Requires Basic Auth of ClientID:Secret)
                String tokenUrl = "https://api.pinterest.com/v5/oauth/token";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                String auth = pinClientId + ":" + pinClientSecret;
                headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString(auth.getBytes()));

                MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
                body.add("grant_type", "authorization_code");
                body.add("code", code);
                body.add("redirect_uri", pinRedirectUri);

                ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, new HttpEntity<>(body, headers), Map.class);
                accessToken = (String) response.getBody().get("access_token");

                // B. Get User Info
                HttpHeaders profileHeaders = new HttpHeaders();
                profileHeaders.setBearerAuth(accessToken);
                ResponseEntity<Map> profile = restTemplate.exchange("https://api.pinterest.com/v5/user_account", HttpMethod.GET, new HttpEntity<>(profileHeaders), Map.class);
                
                platformUserId = (String) profile.getBody().get("username");
                username = (String) profile.getBody().get("username");
            }
            
            // ... (Google/Facebook logic from previous steps goes here) ...

            // C. SAVE TO DB (Unified Table)
            if (accessToken != null) {
                connectionService.connectPlatform(userId, platform.toUpperCase(), accessToken, username != null ? username : platformUserId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect " + platform + ": " + e.getMessage());
        }
    }
}