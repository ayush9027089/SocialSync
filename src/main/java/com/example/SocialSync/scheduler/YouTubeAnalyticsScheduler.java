package com.example.SocialSync.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.SocialSync.repository.YouTubeAccountRepository;
import com.example.SocialSync.service.YouTubeAnalyticsService;

@Component
@RequiredArgsConstructor
public class YouTubeAnalyticsScheduler {

    private final YouTubeAccountRepository accountRepository;
    private final YouTubeAnalyticsService analyticsService;

    @Scheduled(cron = "0 0 2 * * *")
    public void fetchDailyAnalytics() {
        accountRepository.findAll()
                .forEach(analyticsService::fetchAndStoreAnalytics);
    }
}
