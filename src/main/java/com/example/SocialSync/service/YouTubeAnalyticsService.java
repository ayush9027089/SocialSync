package com.example.SocialSync.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.SocialSync.model.YouTubeAccount;
import com.example.SocialSync.model.YouTubeAnalyticsData;
import com.example.SocialSync.repository.YouTubeAnalyticsRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class YouTubeAnalyticsService {

    private final YouTubeAnalyticsRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    @SuppressWarnings("unchecked")
    public void fetchAndStoreAnalytics(YouTubeAccount account) {

        LocalDate yesterday = LocalDate.now().minusDays(1);

        if (repository.existsByChannelIdAndAnalyticsDate(
                account.getChannelId(), yesterday)) return;

        String url =
                "https://youtubeanalytics.googleapis.com/v2/reports" +
                        "?ids=channel==MINE" +
                        "&metrics=views,likes,comments,subscribersGained,subscribersLost" +
                        "&dimensions=day" +
                        "&startDate=" + yesterday +
                        "&endDate=" + yesterday;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(account.getAccessToken());

        ResponseEntity<Map> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        Map.class
                );

        List<List<Object>> rows =
                (List<List<Object>>) response.getBody().get("rows");

        if (rows == null || rows.isEmpty()) return;

        List<Object> r = rows.get(0);

        repository.save(
                YouTubeAnalyticsData.builder()
                        .channelId(account.getChannelId())
                        .analyticsDate(yesterday)
                        .views(((Number) r.get(1)).longValue())
                        .likes(((Number) r.get(2)).longValue())
                        .comments(((Number) r.get(3)).longValue())
                        .subscribersGained(((Number) r.get(4)).longValue())
                        .subscribersLost(((Number) r.get(5)).longValue())
                        .build()
        );
    }
}
