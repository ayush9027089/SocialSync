package com.example.SocialSync.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PinterestConfig {

    @Value("${pinterest.api.base-url}")
    private String baseUrl;

    @Value("${pinterest.access.token}")
    private String accessToken;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }
@Value("${pinterest.client-id:YOUR_DEFAULT_ID}")
private String clientId;

@Value("${pinterest.client-secret}")
private String clientSecret;

@Value("${pinterest.redirect-uri:http://localhost:8082/api/pinterest/callback}")
private String redirectUri;


public String getClientId() { return clientId; }
public String getClientSecret() { return clientSecret; }
public String getRedirectUri() { return redirectUri; }
}

