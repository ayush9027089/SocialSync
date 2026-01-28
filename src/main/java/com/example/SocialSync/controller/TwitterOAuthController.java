package com.example.SocialSync.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.SocialSync.service.TwitterAuthService;

import java.io.IOException;

@RestController
@RequestMapping("/oauth/twitter")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TwitterOAuthController {

    private final TwitterAuthService authService;

    // STEP 1: Frontend asks for Twitter Login URL
    @GetMapping("/connect")
    public ResponseEntity<String> connect(@RequestParam String userId) {
        // We pass userId as "state" to track who is logging in
        String url = authService.getAuthorizationUrl(userId);
        return ResponseEntity.ok(url);
    }

    // STEP 2: Twitter redirects back here
    @GetMapping("/callback")
    public void callback(
            @RequestParam("code") String code,
            @RequestParam("state") String userId,
            HttpServletResponse response
    ) throws IOException {
        
        authService.handleCallback(code, userId);

        // Redirect back to frontend dashboard
        response.sendRedirect("http://127.0.0.1:5500/src/main/resources/static/frontend/main.html?status=twitter_connected");
    }
}