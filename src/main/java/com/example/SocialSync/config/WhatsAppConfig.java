package com.example.SocialSync.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WhatsAppConfig {

    @Value("${whatsapp.token}")
    public String token;

    @Value("${whatsapp.phoneNumberId}")
    public String phoneNumberId;

    @Value("${whatsapp.apiUrl}")
    public String apiUrl;
}
