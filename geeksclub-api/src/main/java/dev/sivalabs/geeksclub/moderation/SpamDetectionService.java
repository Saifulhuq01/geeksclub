package dev.sivalabs.geeksclub.moderation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SpamDetectionService {
    private static final Logger log = LoggerFactory.getLogger(SpamDetectionService.class);

    public SpamDetectionService() {}

    public boolean detectSpam(String content) {
        return false;
    }
}
