package com.example.SocialSync.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Map;

@Service
public class LinkedInMediaService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String uploadMedia(
            String accessToken,
            String ownerUrn,
            File file,
            String recipe
    ) {

        String registerPayload = """
        {
          "registerUploadRequest": {
            "owner": "%s",
            "recipes": ["%s"],
            "serviceRelationships": [{
              "relationshipType": "OWNER",
              "identifier": "urn:li:userGeneratedContent"
            }]
          }
        }
        """.formatted(ownerUrn, recipe);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(
                        "https://api.linkedin.com/v2/assets?action=registerUpload",
                        new HttpEntity<>(registerPayload, headers),
                        Map.class
                );

        Map value = (Map) response.getBody().get("value");
        String asset = (String) value.get("asset");

        Map uploadMechanism =
                (Map) ((Map) value.get("uploadMechanism"))
                        .get("com.linkedin.digitalmedia.uploading.MediaUploadHttpRequest");

        String uploadUrl = (String) uploadMechanism.get("uploadUrl");

        HttpHeaders uploadHeaders = new HttpHeaders();
        uploadHeaders.setBearerAuth(accessToken);
        uploadHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        restTemplate.exchange(
                uploadUrl,
                HttpMethod.PUT,
                new HttpEntity<>(new FileSystemResource(file), uploadHeaders),
                String.class
        );
        return asset;
    }
}
