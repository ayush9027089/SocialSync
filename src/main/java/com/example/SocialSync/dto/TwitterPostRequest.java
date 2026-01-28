package com.example.SocialSync.dto;


import lombok.Data;

@Data
public class TwitterPostRequest {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
