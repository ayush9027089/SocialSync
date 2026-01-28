package com.example.SocialSync.util;


public class AuthEmailTemplateUtil {

    public static String signupSuccess(String username) {
        return """
                Hello %s,

                Your account has been created successfully.

                You can now log in and start managing your social media platforms
                using Social Media Handler.

                Regards,
                Social Media Handler Team
                """.formatted(username);
    }

    public static String loginAlert(String username) {
        return """
                Hello %s,

                A new login was detected on your account.

                If this was not you, please reset your password immediately.

                Regards,
                Social Media Handler Team
                """.formatted(username);
    }
}

