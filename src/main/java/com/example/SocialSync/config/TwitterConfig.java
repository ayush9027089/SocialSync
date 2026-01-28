package com.example.SocialSync.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwitterConfig {

    @Value("${twitter.api.key}")
    private String apiKey;

    @Value("${twitter.api.secret}")
    private String apiSecret;

    @Value("${twitter.access.token}")
    private String accessToken;

    @Value("${twitter.access.token.secret}")
    private String accessTokenSecret;

    @Value("${twitter.bearer.token}")
    private String bearerToken;

    public String getBearerToken() {
        return bearerToken;
    }
}
