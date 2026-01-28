package com.example.SocialSync.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.example.SocialSync.repository.YouTubeAnalyticsRepository;

@RestController
@RequestMapping("/youtube/analytics")
@RequiredArgsConstructor
public class YouTubeAnalyticsController {
    private final YouTubeAnalyticsRepository repository;
    @GetMapping("/{channelId}")
    public Object getAnalytics(@PathVariable String channelId) {
        return repository.findByChannelIdOrderByAnalyticsDateAsc(channelId);
    }
}