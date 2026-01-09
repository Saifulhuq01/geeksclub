package dev.sivalabs.geeksclub.domain.service;

import dev.sivalabs.geeksclub.domain.dto.ActiveUserVM;
import dev.sivalabs.geeksclub.domain.dto.DailyStatistic;
import dev.sivalabs.geeksclub.domain.dto.DailyStatisticsVM;
import dev.sivalabs.geeksclub.domain.dto.MostActiveUsersVM;
import dev.sivalabs.geeksclub.domain.dto.SystemOverviewVM;
import dev.sivalabs.geeksclub.domain.dto.TrendingMessageVM;
import dev.sivalabs.geeksclub.domain.dto.TrendingMessagesVM;
import dev.sivalabs.geeksclub.domain.repo.MessageRepository;
import dev.sivalabs.geeksclub.domain.repo.UserRepository;
import dev.sivalabs.geeksclub.domain.repo.VoteRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public DailyStatisticsVM getDailyStatistics(int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        Instant startInstant = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);

        // Fetch daily stats from repositories
        List<MessageRepository.DailyMessageStats> messageStats = messageRepository.getDailyMessageStats(startInstant);
        List<VoteRepository.DailyVoteStats> voteStats = voteRepository.getDailyVoteStats(startInstant);
        List<UserRepository.DailyActiveUserStats> activeUserStats =
                userRepository.getDailyActiveUserStats(startInstant);

        // Create maps for easy lookup
        Map<LocalDate, Long> messageCountMap = new HashMap<>();
        Map<LocalDate, Long> spamCountMap = new HashMap<>();
        Map<LocalDate, Long> voteCountMap = new HashMap<>();
        Map<LocalDate, Long> activeUserCountMap = new HashMap<>();

        messageStats.forEach(stat -> {
            LocalDate date = stat.getDate().toLocalDate();
            messageCountMap.put(date, stat.getMessageCount());
            spamCountMap.put(date, stat.getSpamCount());
        });

        voteStats.forEach(stat -> {
            LocalDate date = stat.getDate().toLocalDate();
            voteCountMap.put(date, stat.getVoteCount());
        });

        activeUserStats.forEach(stat -> {
            LocalDate date = stat.getDate().toLocalDate();
            activeUserCountMap.put(date, stat.getActiveUserCount());
        });

        // Build daily statistics list
        List<DailyStatistic> statistics = new ArrayList<>();
        for (LocalDate date = endDate; !date.isBefore(startDate); date = date.minusDays(1)) {
            statistics.add(new DailyStatistic(
                    date,
                    messageCountMap.getOrDefault(date, 0L).intValue(),
                    activeUserCountMap.getOrDefault(date, 0L).intValue(),
                    spamCountMap.getOrDefault(date, 0L).intValue(),
                    voteCountMap.getOrDefault(date, 0L).intValue()));
        }

        DailyStatisticsVM.Period period = new DailyStatisticsVM.Period(startDate, endDate, days);
        return new DailyStatisticsVM(statistics, period);
    }

    public MostActiveUsersVM getMostActiveUsers(int limit) {
        List<UserRepository.ActiveUserStats> activeUserStats = userRepository.getMostActiveUsers(limit);

        List<ActiveUserVM> users = activeUserStats.stream()
                .map(stat -> new ActiveUserVM(
                        stat.getUserId(),
                        stat.getFullName(),
                        stat.getUsername(),
                        stat.getEmail(),
                        stat.getMessageCount(),
                        stat.getVoteCount(),
                        stat.getTotalActivity(),
                        stat.getLastActivityAt(),
                        stat.getJoinedAt()))
                .toList();

        return new MostActiveUsersVM(users, limit);
    }

    public TrendingMessagesVM getTrendingMessages(int limit, int days) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(days);
        Instant startInstant = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);

        List<MessageRepository.TrendingMessageStats> trendingStats =
                messageRepository.getTrendingMessages(startInstant, limit);

        List<TrendingMessageVM> messages = trendingStats.stream()
                .map(stat -> new TrendingMessageVM(
                        stat.getMessageId(),
                        stat.getContent(),
                        new TrendingMessageVM.AuthorInfo(
                                stat.getAuthorId(), stat.getAuthorFullName(), stat.getAuthorUsername()),
                        stat.getUpvoteCount(),
                        stat.getDownvoteCount(),
                        stat.getScore(),
                        stat.getRecentVotes(),
                        stat.getCreatedAt()))
                .toList();

        TrendingMessagesVM.Period period = new TrendingMessagesVM.Period(days, startDate);
        return new TrendingMessagesVM(messages, period);
    }
}
