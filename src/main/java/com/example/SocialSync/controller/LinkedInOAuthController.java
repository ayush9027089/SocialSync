package com.example.SocialSync.controller;


import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.SocialSync.service.LinkedInAuthService;

import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/oauth/linkedin")
@RequiredArgsConstructor
public class LinkedInOAuthController {

    public final LinkedInAuthService linkedInAuthService;

    /**
     * STEP 1: Redirect user to LinkedIn authorization page
     */
    @GetMapping("/connect")
public void connect(
        @RequestParam String userId,
        HttpServletResponse response
) throws IOException {

    String authUrl = linkedInAuthService.getAuthorizationUrl(userId);

    // 🔥 THIS IS THE FIX
    response.sendRedirect(authUrl);
}


    /**
     * STEP 2: LinkedIn redirects here after user consent
     * NOTE: userId is passed temporarily for testing
     */

    // @GetMapping("/callback")
    // public ResponseEntity<String> callback(@RequestParam("code") String code, @RequestParam("state") String userId) {
    //     linkedInAuthService.handleCallback(code, userId);
    //     return ResponseEntity.ok("LinkedIn connected successfully");
    // }
    @GetMapping("/callback")
    public void callback(
            @RequestParam("code") String code, 
            @RequestParam("state") String userId,
            HttpServletResponse response // Needed for redirection
    ) throws IOException {
        
        // 1. Process the auth (Exchange code for token, save to DB)
        linkedInAuthService.handleCallback(code, userId);

        // 2. Redirect user back to the Frontend Dashboard
        // Adjust this URL to match your specific frontend dashboard path
        response.sendRedirect("http://127.0.0.1:5500/src/main/resources/static/frontend/main.html?status=linkedin_connected");
    }
}

