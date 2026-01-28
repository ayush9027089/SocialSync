package com.example.SocialSync.util;


public class EmailTemplateUtil {

    public static String postSuccess(
            String username,
            String platform,
            String time
    ) {
        return """
                Hello %s,

                Your post has been successfully published on %s.

                Scheduled Time: %s

                Thank you,
                Social Media Handler System
                """.formatted(username, platform, time);
    }

    public static String postFailure(
            String username,
            String platform,
            String reason
    ) {
        return """
                Hello %s,

                Your scheduled post failed on %s.

                Reason:
                %s

                The system will retry automatically if enabled.

                Regards,
                Social Media Handler System
                """.formatted(username, platform, reason);
    }

    public static String accountStatus(
            String username,
            String status
    ) {
        return """
                Hello %s,

                Your account status has been updated.

                Current Status: %s

                Regards,
                Social Media Handler System
                """.formatted(username, status);
    }
}

