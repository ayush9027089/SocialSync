package com.example.SocialSync.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SocialSync.model.LinkedInScheduledPost;
import com.example.SocialSync.repository.LinkedInScheduledPostRepository;
import com.example.SocialSync.service.LinkedInAnalyticsService;


@RestController
@RequestMapping("/linkedin/analytics")
@RequiredArgsConstructor
public class LinkedInAnalyticsController {

    private final LinkedInAnalyticsService analyticsService;
    private final LinkedInScheduledPostRepository postRepository;

    @GetMapping("/{postId}")
    public ResponseEntity<String> getAnalytics(@PathVariable String postId) {

        LinkedInScheduledPost post =
                postRepository.findById(postId)
                        .orElseThrow();

        return ResponseEntity.ok(
                analyticsService.fetchPostAnalytics(
                        post.getAccount().getAccessToken(),
                        post.getPlatformPostUrn()
                )
        );
    }
}

