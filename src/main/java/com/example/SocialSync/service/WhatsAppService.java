package com.example.SocialSync.service;


import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.SocialSync.config.WhatsAppConfig;

import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsAppService {

    private final WhatsAppConfig config;
    private final RestTemplate restTemplate = new RestTemplate();

    public WhatsAppService(WhatsAppConfig config) {
        this.config = config;
    }

    public String sendMessage(String accessToken, String phoneNumberId, String to, String message) {
        
        // Use user's phone ID if available, otherwise fallback or fail
        String usePhoneId = (phoneNumberId != null && !phoneNumberId.isEmpty()) ? phoneNumberId : config.phoneNumberId;
        String url = config.apiUrl + "/" + usePhoneId + "/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken); // ✅ Use User's Token

        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", to);
        body.put("type", "text");

        Map<String, String> text = new HashMap<>();
        text.put("body", message);
        body.put("text", text);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("WhatsApp API Error: " + e.getMessage());
        }
    }

    public String sendMessage(String to, String message) {

        String url = config.apiUrl + "/" + config.phoneNumberId + "/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(config.token);

        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", to);
        body.put("type", "text");

        Map<String, String> text = new HashMap<>();
        text.put("body", message);

        body.put("text", text);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(url, request, String.class);

        return response.getBody();
    }

    
}
