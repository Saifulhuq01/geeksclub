package dev.sivalabs.geeksclub.moderation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SpamDetectionService {
    private static final Logger log = LoggerFactory.getLogger(SpamDetectionService.class);
    private final ChatClient chatClient;

    public SpamDetectionService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public boolean detectSpam(String content) {
        if (content == null || content.isBlank()) {
            return false;
        }

        String prompt = """
                Analyze the following text and determine if it is spam.
                Respond with only 'true' if it is spam, and 'false' otherwise.

                Text:
                %s
                """.formatted(content);

        try {
            String response = chatClient.prompt().user(prompt).call().content();
            log.debug("AI response for spam detection: {}", response);
            return Boolean.parseBoolean(response.trim().toLowerCase());
        } catch (Exception e) {
            log.error("Failed to detect spam using AI", e);
            return false;
        }
    }
}
