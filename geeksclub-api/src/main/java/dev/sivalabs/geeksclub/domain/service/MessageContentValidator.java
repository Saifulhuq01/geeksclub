package dev.sivalabs.geeksclub.domain.service;

import dev.sivalabs.geeksclub.domain.exception.BadRequestException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class MessageContentValidator {

    private static final Pattern SCRIPT_PATTERN = Pattern.compile("<script", Pattern.CASE_INSENSITIVE);
    private static final Pattern HTML_PATTERN = Pattern.compile("<[^>]+>");
    private static final int MAX_URL_COUNT = 5;

    public void validate(String content) {
        // Check for script tags
        if (SCRIPT_PATTERN.matcher(content).find()) {
            throw new BadRequestException("Message content contains disallowed HTML scripts");
        }

        // Count URLs
        int urlCount = countUrls(content);
        if (urlCount > MAX_URL_COUNT) {
            throw new BadRequestException("Message contains too many URLs (max: " + MAX_URL_COUNT + ")");
        }

        // Strip HTML if present (optional)
        if (HTML_PATTERN.matcher(content).find()) {
            // Either reject or sanitize
            throw new BadRequestException("HTML content is not allowed");
        }
    }

    private int countUrls(String content) {
        Pattern urlPattern = Pattern.compile("https?://[^\\s]+");
        Matcher matcher = urlPattern.matcher(content);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}
