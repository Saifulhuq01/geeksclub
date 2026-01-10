package dev.sivalabs.geeksclub.domain.repo;

import dev.sivalabs.geeksclub.domain.dto.VoteCounts;
import dev.sivalabs.geeksclub.domain.entity.VoteEntity;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteRepository extends JpaRepository<VoteEntity, Long> {

    @Query("""
            SELECT v.messageId as messageId,
                   SUM(CASE WHEN v.voteType = 'UP' THEN 1 ELSE 0 END) as upvoteCount,
                   SUM(CASE WHEN v.voteType = 'DOWN' THEN 1 ELSE 0 END) as downvoteCount
            FROM VoteEntity v
            WHERE v.messageId IN :messageIds
            GROUP BY v.messageId
            """)
    List<VoteCount> getVoteCountsByMessageIds(@Param("messageIds") List<Long> messageIds);

    @Query("SELECT v FROM VoteEntity v WHERE v.messageId IN :messageIds AND v.userId = :userId")
    List<VoteEntity> findByMessageIdsAndUserId(
            @Param("messageIds") List<Long> messageIds, @Param("userId") Long userId);

    Optional<VoteEntity> findByMessageIdAndUserId(Long messageId, Long userId);

    @Query("select count(v) from VoteEntity v where v.messageId = :messageId and v.voteType = 'UP'")
    int countUpVotes(@Param("messageId") Long messageId);

    @Query("select count(v) from VoteEntity v where v.messageId = :messageId and v.voteType = 'DOWN'")
    int countDownVotes(@Param("messageId") Long messageId);

    @Query("SELECT COUNT(v) FROM VoteEntity v WHERE v.createdAt >= :since")
    long countVotesCreatedSince(@Param("since") Instant since);

    @Query(value = """
            SELECT DATE(created_at) as date, COUNT(*) as voteCount
            FROM votes
            WHERE created_at >= :startDate
            GROUP BY DATE(created_at)
            ORDER BY date DESC
            """, nativeQuery = true)
    List<DailyVoteStats> getDailyVoteStats(@Param("startDate") Instant startDate);

    @Query("""
    SELECT new dev.sivalabs.geeksclub.domain.dto.VoteCounts(
        SUM(CASE WHEN v.voteType = 'UP' THEN 1 ELSE 0 END),
        SUM(CASE WHEN v.voteType = 'DOWN' THEN 1 ELSE 0 END)
    )
    FROM VoteEntity v
    WHERE v.messageId = :messageId
    """)
    Optional<VoteCounts> getVoteCounts(@Param("messageId") Long messageId);

    interface VoteCount {
        Long getMessageId();

        Long getUpvoteCount();

        Long getDownvoteCount();
    }

    interface DailyVoteStats {
        LocalDate getDate();

        long getVoteCount();
    }
}
