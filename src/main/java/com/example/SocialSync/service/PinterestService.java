package com.example.SocialSync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.SocialSync.config.PinterestConfig;

import java.util.HashMap;
import java.util.Map;

@Service
public class PinterestService {

    @Autowired
    private PinterestConfig pinterestConfig;

    private final RestTemplate restTemplate = new RestTemplate();

    // Fix: Ye method Scheduler call karega
         public boolean createPin(String accessToken, String title, String description, String boardId, String imageUrl, String destinationLink) {
            try {
                String url = pinterestConfig.getBaseUrl() + "/pins";

                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(accessToken); // ✅ Uses the dynamic token passed from Scheduler
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
    public String getUserAccount() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(pinterestConfig.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                pinterestConfig.getBaseUrl() + "/user_account",
                HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
public String getPinterestAuthUrl() {
    return "https://www.pinterest.com/oauth/?" +
            "client_id=" + pinterestConfig.getClientId() +
            "&redirect_uri=" + pinterestConfig.getRedirectUri() +
            "&response_type=code" +
            "&scope=boards:read,pins:read,pins:write";
}
}