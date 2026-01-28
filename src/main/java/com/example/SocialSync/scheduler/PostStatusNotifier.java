package com.example.SocialSync.scheduler;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.example.SocialSync.model.User;
import com.example.SocialSync.service.EmailService;
import com.example.SocialSync.util.EmailTemplateUtil;


@Component
@RequiredArgsConstructor
public class PostStatusNotifier {

    private final EmailService emailService;

    public void notifyPostSuccess(User user, String platform, String time) {
        emailService.sendEmail(
                user.getEmail(),
                "✅ Post Published Successfully",
                EmailTemplateUtil.postSuccess(
                        user.getUsername(),
                        platform,
                        time
                )
        );
    }

    public void notifyPostFailure(User user, String platform, String reason) {
        emailService.sendEmail(
                user.getEmail(),
                "❌ Post Failed",
                EmailTemplateUtil.postFailure(
                        user.getUsername(),
                        platform,
                        reason
                )
        );
    }
}

