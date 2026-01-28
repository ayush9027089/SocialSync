package com.example.SocialSync.util;

public class PasswordEmailTemplateUtil {

    public static String resetPassword(String username, String link) {
        return """
                Hello %s,

                We received a request to reset your password.

                Click the link below to reset your password:
                %s

                This link will expire in 15 minutes.

                If you did not request this, please ignore this email.

                Regards,
                Social Media Handler Team
                """.formatted(username, link);
    }
}

