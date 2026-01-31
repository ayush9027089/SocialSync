package com.example.SocialSync.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import com.example.SocialSync.dto.PinterestPinDTO;
import com.example.SocialSync.model.PinterestScheduledPost;
import com.example.SocialSync.model.User;
import com.example.SocialSync.repository.PinterestScheduledPostRepository;
import com.example.SocialSync.repository.UserRepository;
import com.example.SocialSync.service.PinterestService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/oauth/pinterest")
@RequiredArgsConstructor
public class PinterestOAuthController {
@Autowired
    private  PinterestService pinterestService;
    @Autowired
    private  PinterestScheduledPostRepository repository;
    @Autowired
    private UserRepository userRepository;

    // =====================
    // OAUTH FLOW
    // =====================
    private String getLoggedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = null;
        if (auth.getPrincipal() instanceof User) {
            email = ((User) auth.getPrincipal()).getEmail();
        } else if (auth.getPrincipal() instanceof UserDetails) {
            email = ((UserDetails) auth.getPrincipal()).getUsername();
        } else {
            email = auth.getPrincipal().toString();
        }
        
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

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
