package com.example.SocialSync.controller;


import org.springframework.web.bind.annotation.*;

import com.example.SocialSync.dto.WhatsAppMessageRequest;
import com.example.SocialSync.service.WhatsAppService;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsAppController {

    private final WhatsAppService service;

    public WhatsAppController(WhatsAppService service) {
        this.service = service;
    }

    @PostMapping("/send")
    public String send(@RequestBody WhatsAppMessageRequest request) {
        return service.sendMessage(
                request.getTo(),
                request.getMessage()
        );
    }
}
