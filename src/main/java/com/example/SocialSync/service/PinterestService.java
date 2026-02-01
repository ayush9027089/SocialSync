package com.example.SocialSync.service;


import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PinterestService {

    // Standard Pinterest API URL
    private final String BASE_URL = "https://api.pinterest.com/v5";

    private final RestTemplate restTemplate = new RestTemplate();
    // Fix: Ye method Scheduler call karega
        public boolean createPin(String accessToken, String title, String description, String boardId, String imageUrl, String destinationLink) {
        try {
            String url = BASE_URL + "/pins";

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();
            body.put("title", title);
            body.put("description", description);
            body.put("board_id", boardId);
            body.put("link", destinationLink);

            Map<String, String> mediaSource = new HashMap<>();
            mediaSource.put("source_type", "image_url");
            mediaSource.put("url", imageUrl);
            body.put("media_source", mediaSource);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            return response.getStatusCode() == HttpStatus.CREATED;
        } catch (Exception e) {
            System.err.println("Pinterest API Error: " + e.getMessage());
            return false;
        }
    }
    public String getUserAccount(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                BASE_URL + "/user_account",
                HttpMethod.GET, entity, String.class);
        return response.getBody();
        }
}