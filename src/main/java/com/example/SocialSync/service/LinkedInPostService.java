package com.example.SocialSync.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LinkedInPostService {

    private final RestTemplate restTemplate = new RestTemplate();
    public String postWithMedia(String accessToken, String authorUrn, String text, String assetUrn, String mediaCategory) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = """
        {
          "author": "%s",
          "lifecycleState": "PUBLISHED",
          "specificContent": {
            "com.linkedin.ugc.ShareContent": {
              "shareCommentary": { "text": "%s" },
              "shareMediaCategory": "%s",
              "media": [{
                "status": "READY",
                "media": "%s"
              }]
            }
          },
          "visibility": {
            "com.linkedin.ugc.MemberNetworkVisibility": "PUBLIC"
          }
        }
        """.formatted(authorUrn, text, mediaCategory, assetUrn);
        ResponseEntity<Map> response = restTemplate.postForEntity("https://api.linkedin.com/v2/ugcPosts", new HttpEntity<>(body, headers), Map.class);
        return response.getHeaders().getFirst("x-restli-id");
    }
}
