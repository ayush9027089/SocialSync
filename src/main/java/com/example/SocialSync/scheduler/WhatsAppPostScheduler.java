package com.example.SocialSync.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.SocialSync.model.WhatsAppScheduledPost;
import com.example.SocialSync.repository.WhatsAppScheduledPostRepository;
import com.example.SocialSync.service.WhatsAppService;

@Service
public class WhatsAppPostScheduler {

    private final WhatsAppScheduledPostRepository repository;
    private final WhatsAppService whatsappService;

    public WhatsAppPostScheduler(WhatsAppScheduledPostRepository repository, WhatsAppService whatsappService) {
        this.repository = repository;
        this.whatsappService = whatsappService;
    }

    @Scheduled(fixedRate = 60000)
    public void runAutomation() {
        List<WhatsAppScheduledPost> pendingList = repository.findByStatusAndScheduledTimeBefore("PENDING", LocalDateTime.now());

        for (WhatsAppScheduledPost post : pendingList) {
            try {
                if ("WHATSAPP".equalsIgnoreCase(post.getPlatform())) {
                    whatsappService.sendMessage(post.getRecipient(), post.getContent());
                }
                // Agar post successfully chali gayi
                post.setStatus("SENT"); 
            } catch (Exception e) {
                post.setStatus("FAILED");
            }
            repository.save(post);
        }
    }
}