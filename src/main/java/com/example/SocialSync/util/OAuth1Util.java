package com.example.SocialSync.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class OAuth1Util {

    public static String generateOAuthHeader(
            String consumerKey,
            String consumerSecret,
            String accessToken,
            String accessTokenSecret,
            String url,
            String method) {

        String nonce = UUID.randomUUID().toString().replaceAll("-", "");
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        String baseParams =
                "oauth_consumer_key=" + consumerKey +
                        "&oauth_nonce=" + nonce +
                        "&oauth_signature_method=HMAC-SHA1" +
                        "&oauth_timestamp=" + timestamp +
                        "&oauth_token=" + accessToken +
                        "&oauth_version=1.0";

        String baseString = method + "&" +
                encode(url) + "&" +
                encode(baseParams);

        String signingKey =
                encode(consumerSecret) + "&" +
                        encode(accessTokenSecret);

        String signature = hmacSha1(baseString, signingKey);

        return "OAuth " +
                "oauth_consumer_key=\"" + consumerKey + "\", " +
                "oauth_nonce=\"" + nonce + "\", " +
                "oauth_signature=\"" + encode(signature) + "\", " +
                "oauth_signature_method=\"HMAC-SHA1\", " +
                "oauth_timestamp=\"" + timestamp + "\", " +
                "oauth_token=\"" + accessToken + "\", " +
                "oauth_version=\"1.0\"";
    }

    private static String hmacSha1(String value, String key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(
                    key.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA1"
            );
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(keySpec);
            return Base64.getEncoder().encodeToString(
                    mac.doFinal(value.getBytes(StandardCharsets.UTF_8))
            );
        } catch (Exception e) {
            throw new RuntimeException("OAuth signature failed", e);
        }
    }

    private static String encode(String value) {
        try {
                return URLEncoder.encode(value, StandardCharsets.UTF_8.toString())
                .replace("+", "%20")
                .replace("*", "%2A")
                .replace("%7E", "~");
        } catch (Exception e) {
                throw new RuntimeException(e);
        }
}
}