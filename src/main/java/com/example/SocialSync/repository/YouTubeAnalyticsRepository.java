package com.example.SocialSync.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.SocialSync.model.YouTubeAnalyticsData;

import java.time.LocalDate;
import java.util.List;
public interface YouTubeAnalyticsRepository extends MongoRepository<YouTubeAnalyticsData, String> {
List<YouTubeAnalyticsData> findByChannelIdOrderByAnalyticsDateAsc(String channelId);
boolean existsByChannelIdAndAnalyticsDate(String channelId, LocalDate date);
}