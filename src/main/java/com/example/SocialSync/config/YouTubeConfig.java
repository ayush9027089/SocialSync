package com.example.SocialSync.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class YouTubeConfig {

    @Value("${youtube.client-id}")
    public String clientId;

    @Value("${youtube.client-secret}")
    public String clientSecret;

    @Value("${youtube.redirect-uri}")
    public String redirectUri;
}
