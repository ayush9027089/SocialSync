package com.example.SocialSync.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.SocialSync.model.LinkedInScheduledPost;
import com.example.SocialSync.model.PostStatus;
import com.example.SocialSync.model.SocialConnection;
import com.example.SocialSync.repository.LinkedInScheduledPostRepository;
import com.example.SocialSync.repository.SocialConnectionRepository;
import com.example.SocialSync.repository.UserRepository;
import com.example.SocialSync.service.EmailService;
import com.example.SocialSync.service.LinkedInAnalyticsService;
import com.example.SocialSync.service.LinkedInPostService;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LinkedInPostScheduler {

    private final LinkedInScheduledPostRepository postRepository;
    private final LinkedInAnalyticsService mediaService;
    private final LinkedInPostService postService;
    private final EmailService emailService;
    private final SocialConnectionRepository socialRepository; // ✅ Inject this
    private final UserRepository userRepository; // To get email for notifications

    @Scheduled(cron = "0 * * * * *")
    public void publishPosts() {
        List<LinkedInScheduledPost> posts = postRepository.findByStatusAndScheduledAtLessThanEqual(
                        PostStatus.PENDING, LocalDateTime.now());

        for (LinkedInScheduledPost post : posts) {
            try {
                // 1. Fetch Token from Unified Table
                SocialConnection connection = socialRepository.findByUserIdAndPlatform(post.getUserId(), "LINKEDIN")
                        .orElseThrow(() -> new RuntimeException("LinkedIn not connected"));
                
                String accessToken = connection.getAccessToken();
                String linkedinUserId = connection.getPlatformUserId(); // Need this for URNs

                String ownerUrn = "urn:li:person:" + linkedinUserId;

                // 2. Upload Media (Pass accessToken)
                String assetUrn = mediaService.uploadMedia(
                                accessToken,
                                ownerUrn,
                                new File(post.getMediaPath()),
                                "urn:li:digitalmediaRecipe:feedshare-image"
                        );

                // 3. Post (Pass accessToken)
                String postUrn = postService.postWithMedia(
                                accessToken,
                                ownerUrn,
                                post.getContent(),
                                assetUrn,
                                "IMAGE"
                        );

                post.setPlatformPostUrn(postUrn);
                post.setStatus(PostStatus.POSTED);
                post.setPostedAt(LocalDateTime.now());
                
                // Optional: Send Email
                // User user = userRepository.findById(post.getUserId()).orElse(null);
                // if(user != null) emailService.sendEmail(...)

            } catch (Exception e) {
                post.setStatus(PostStatus.FAILED);
                post.setFailureReason(e.getMessage());
            }
            postRepository.save(post);
        }
    }
}