package com.example.SocialSync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.SocialSync.dto.ForgotPasswordRequest;
import com.example.SocialSync.dto.LoginRequest;
import com.example.SocialSync.dto.LoginResponse;
import com.example.SocialSync.dto.ResetPasswordRequest;
import com.example.SocialSync.dto.SignupRequest;
import com.example.SocialSync.dto.SignupResponse;
import com.example.SocialSync.service.AuthService;



@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ===============================
    // LOGIN AND SIGNUP
    // ===============================

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest request) {
        SignupResponse response=authService.signupUser(request);
        if (!response.isSuccess()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.loginUser(request);
        return ResponseEntity.ok(response);
    }

    // ===============================
    // FORGOT PASSWORD
    // ===============================

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok("Password reset email sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request){
        authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successful");
    }
}
