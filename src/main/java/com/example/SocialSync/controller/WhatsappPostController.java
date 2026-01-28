package com.example.SocialSync.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SocialSync.model.WhatsAppScheduledPost;
import com.example.SocialSync.repository.WhatsAppScheduledPostRepository;



@RestController
@RequestMapping("/api/posts")
public class WhatsappPostController {

    private final WhatsAppScheduledPostRepository repository;

    public WhatsappPostController(WhatsAppScheduledPostRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/schedule")
    public WhatsAppScheduledPost scheduleNewPost(@RequestBody WhatsAppScheduledPost post) {
        post.setStatus("PENDING");
        return repository.save(post);
    }

    @GetMapping("/all")
    public List<WhatsAppScheduledPost> getAllPosts() {
        return repository.findAll();
    }
}