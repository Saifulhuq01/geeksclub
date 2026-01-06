package dev.sivalabs.geeksclub.moderation;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SpamDetectionService {

    private final ChatClient chatClient;

    public SpamDetectionService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public boolean detectSpam(String content) {
        String prompt = """
                Analyze the following text and determine if it is spam.
                Respond with only 'true' if it is spam, and 'false' otherwise.
                
                Text:
                %s
                """.formatted(content);
        
        try {
            String response = chatClient.prompt().user(prompt).call().content();
            return Boolean.parseBoolean(response.trim().toLowerCase());
        } catch (Exception e) {
            // Fallback or log error
            return false;
        }
    }
}
