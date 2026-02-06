package com.example.SocialSync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SocialSync.repository.WhatsAppScheduledPostRepository;

@Service
public class PostService {
    @Autowired
    private WhatsAppScheduledPostRepository whatsAppScheduledPostRepository;

    public long countAllPosts(){
        return whatsAppScheduledPostRepository.count();
    }
}
