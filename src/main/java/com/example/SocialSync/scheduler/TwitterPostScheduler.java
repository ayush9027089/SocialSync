package com.example.SocialSync.scheduler;



import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.SocialSync.model.TwitterPost;
import com.example.SocialSync.repository.TwitterPostRepository;
import com.example.SocialSync.service.TwitterService;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TwitterPostScheduler {

    private final TwitterPostRepository repository;
    private final TwitterService twitterService;

    public TwitterPostScheduler(
            TwitterPostRepository repository,
            TwitterService twitterService) {
        this.repository = repository;
        this.twitterService = twitterService;
    }

    @Scheduled(fixedRate = 60000) // every 1 minute
    public void postScheduledTweets() {

        List<TwitterPost> posts =
                repository.findByStatusAndScheduledTimeBefore(
                        "PENDING",
                        LocalDateTime.now()
                );

        for (TwitterPost post : posts) {
            try {
                // ✅ 1. Check if account is linked
                if (post.getAccount() == null) {
                    post.setStatus("FAILED");
                    // post.setFailureReason("No Twitter account linked");
                    repository.save(post);
                    continue;
                }

                // ✅ 2. Get that specific user's token
                String userAccessToken = post.getAccount().getAccessToken();

                // ✅ 3. Post using that token
                String response = twitterService.postTweet(userAccessToken, post.getContent());

                post.setStatus("POSTED");
                post.setPostedAt(LocalDateTime.now());
                
                // Optional: Save the Tweet ID from response if needed
                
            } catch (Exception e) {
                post.setStatus("FAILED");
                System.err.println("Tweet Failed: " + e.getMessage());
            }
            repository.save(post);
        }
    }
}
