package com.example.SocialSync.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.SocialSync.model.PinterestScheduledPost;
import com.example.SocialSync.model.SocialConnection;
import com.example.SocialSync.repository.PinterestScheduledPostRepository;
import com.example.SocialSync.repository.SocialConnectionRepository;
import com.example.SocialSync.service.PinterestService;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PinterestPostScheduler {

    @Autowired
    private PinterestScheduledPostRepository repository;

    @Autowired
    private PinterestService pinterestService;
    
    @Autowired 
    private SocialConnectionRepository socialRepository;

    @Scheduled(cron = "0 * * * * *")
    public void processScheduledPins() {
        // Current time ke hisab se PENDING posts nikalna
        List<PinterestScheduledPost> pendingPosts = repository.findByStatusAndScheduledTimeBefore("PENDING", LocalDateTime.now());

       for (PinterestScheduledPost post : pendingPosts) {
            try {
                // 1. Find the User who created the post (You need userId in your Post model!)
                // Assuming you add 'userId' to PinterestScheduledPost
                String userId = post.getUserId(); 

                // 2. Get their Token from SocialConnection
                SocialConnection connection = socialRepository.findByUserIdAndPlatform(userId, "PINTEREST")
                        .orElseThrow(() -> new RuntimeException("Pinterest not connected"));

                String accessToken = connection.getAccessToken();

                // 3. Post using that token
                boolean isSuccess = pinterestService.createPin(
                        accessToken, // Pass token dynamically
                        post.getTitle(),
                        post.getDescription(),
                        post.getBoardId(),
                        post.getImageUrl(),
                        post.getDestinationLink()
                );

                if (isSuccess) post.setStatus("PUBLISHED");
                else post.setStatus("FAILED");

            } catch (Exception e) {
                post.setStatus("FAILED");
                post.setErrorMessage(e.getMessage());
            }
            repository.save(post);
        }
    }
}