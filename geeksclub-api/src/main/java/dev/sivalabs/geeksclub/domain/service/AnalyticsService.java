package dev.sivalabs.geeksclub.domain.service;

import dev.sivalabs.geeksclub.domain.dto.SystemOverviewVM;
import dev.sivalabs.geeksclub.domain.repo.MessageRepository;
import dev.sivalabs.geeksclub.domain.repo.UserRepository;
import dev.sivalabs.geeksclub.domain.repo.VoteRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AnalyticsService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final VoteRepository voteRepository;

    public AnalyticsService(
            UserRepository userRepository, MessageRepository messageRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.voteRepository = voteRepository;
    }

    public SystemOverviewVM getSystemOverview() {
        Instant now = Instant.now();
        Instant last7Days = now.minus(7, ChronoUnit.DAYS);
        Instant last24Hours = now.minus(24, ChronoUnit.HOURS);

        long totalUsers = userRepository.count();
        long activeUsersLast7Days = userRepository.countUsersCreatedSince(last7Days);

        long totalMessages = messageRepository.count();
        long messagesLast24Hours = messageRepository.countMessagesCreatedSince(last24Hours);

        long totalVotes = voteRepository.count();
        long votesLast24Hours = voteRepository.countVotesCreatedSince(last24Hours);

        long spamDetected = messageRepository.countSpamMessages();
        double spamRate = totalMessages > 0 ? (double) spamDetected / totalMessages : 0.0;

        return new SystemOverviewVM(
                totalUsers,
                activeUsersLast7Days,
                totalMessages,
                messagesLast24Hours,
                totalVotes,
                votesLast24Hours,
                spamDetected,
                spamRate,
                now);
    }
}
