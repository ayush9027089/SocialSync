package com.example.SocialSync.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import com.example.SocialSync.dto.PinterestPinDTO;
import com.example.SocialSync.model.PinterestScheduledPost;
import com.example.SocialSync.repository.PinterestScheduledPostRepository;
import com.example.SocialSync.service.PinterestService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/oauth/pinterest")
@RequiredArgsConstructor
public class PinterestOAuthController {
@Autowired
    private  PinterestService pinterestService;
    @Autowired
    private  PinterestScheduledPostRepository repository;

    // =====================
    // OAUTH FLOW
    // =====================

    @GetMapping("/connect")
    public void connect(HttpServletResponse response) throws IOException {
        String authUrl = pinterestService.getPinterestAuthUrl();
        response.sendRedirect(authUrl);
    }

    @GetMapping("/callback")
    public void callback(
            @RequestParam("code") String code,
            HttpServletResponse response
    ) throws IOException {

        // Optional for demo
        // pinterestService.handleCallback(code);

        response.sendRedirect(
            "http://127.0.0.1:5500/src/main/resources/static/frontend/main.html?pinterest=connected"
        );
    }

    // =====================
    // SCHEDULING API (KEEP)
    // =====================

    @PostMapping("/schedule")
    public void schedulePin(@RequestBody PinterestPinDTO dto) {

        PinterestScheduledPost post = new PinterestScheduledPost();
        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        post.setBoardId(dto.getBoardId());
        post.setImageUrl(dto.getImageUrl());
        post.setDestinationLink(dto.getDestinationLink());
        post.setScheduledTime(dto.getScheduledTime());
        post.setStatus("PENDING");

        repository.save(post);
    }
}
