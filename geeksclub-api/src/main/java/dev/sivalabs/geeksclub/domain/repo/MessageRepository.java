package dev.sivalabs.geeksclub.domain.repo;

import dev.sivalabs.geeksclub.domain.entity.MessageEntity;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query("""
            SELECT m FROM MessageEntity m
            WHERE m.status = 'PUBLISHED'
            ORDER BY m.createdAt DESC
            """)
    Page<MessageEntity> findAllPublishedOrderByCreatedAtDesc(Pageable pageable);

    @Query("""
            SELECT m FROM MessageEntity m
            LEFT JOIN VoteEntity v ON v.messageId = m.id
            WHERE m.status = 'PUBLISHED'
            GROUP BY m.id, m.userId, m.content, m.isSpam, m.spamConfidence, m.status, m.createdAt, m.updatedAt, m.version
            ORDER BY SUM(CASE WHEN v.voteType = 'UP' THEN 1 ELSE 0 END) DESC, m.createdAt DESC
            """)
    Page<MessageEntity> findAllPublishedOrderByUpvotesDesc(Pageable pageable);

    @Query("""
            SELECT m FROM MessageEntity m
            LEFT JOIN VoteEntity v ON v.messageId = m.id
            WHERE m.status = 'PUBLISHED'
            GROUP BY m.id, m.userId, m.content, m.isSpam, m.spamConfidence, m.status, m.createdAt, m.updatedAt, m.version
            ORDER BY SUM(CASE WHEN v.voteType = 'DOWN' THEN 1 ELSE 0 END) DESC, m.createdAt DESC
            """)
    Page<MessageEntity> findAllPublishedOrderByDownvotesDesc(Pageable pageable);

    @Query(value = """
            SELECT m.* FROM messages m
            LEFT JOIN votes v ON v.message_id = m.id AND v.created_at > CURRENT_TIMESTAMP - INTERVAL '7 days'
            WHERE m.status = 'PUBLISHED'
            GROUP BY m.id
            ORDER BY COUNT(v.id) DESC, m.created_at DESC
            """, countQuery = """
            SELECT COUNT(DISTINCT m.id) FROM messages m
            WHERE m.status = 'PUBLISHED'
            """, nativeQuery = true)
    Page<MessageEntity> findAllPublishedOrderByTrending(Pageable pageable);

    @Query("""
            SELECT m FROM MessageEntity m
            JOIN UserEntity u ON m.userId = u.id
            WHERE u.username = :username AND m.status = 'PUBLISHED'
            ORDER BY m.createdAt DESC
            """)
    Page<MessageEntity> findByUsernameOrderByCreatedAtDesc(String username, Pageable pageable);

    @Query("""
            SELECT m FROM MessageEntity m
            WHERE lower(m.content) LIKE lower(concat('%', :query, '%'))
            AND m.status = 'PUBLISHED'
            ORDER BY m.createdAt DESC
            """)
    Page<MessageEntity> searchMessages(String query, Pageable pageable);

    @Query("SELECT COUNT(m) FROM MessageEntity m WHERE m.createdAt >= :since")
    long countMessagesCreatedSince(@Param("since") Instant since);

    @Query("SELECT COUNT(m) FROM MessageEntity m WHERE m.isSpam = true")
    long countSpamMessages();

    @Query(value = """
            SELECT DATE(created_at) as date,
                   COUNT(*) as messageCount,
                   COUNT(*) FILTER (WHERE is_spam = true) as spamCount
            FROM messages
            WHERE created_at >= :startDate
            GROUP BY DATE(created_at)
            ORDER BY date DESC
            """, nativeQuery = true)
    List<DailyMessageStats> getDailyMessageStats(@Param("startDate") Instant startDate);

    interface DailyMessageStats {
        java.sql.Date getDate();

        long getMessageCount();

        long getSpamCount();
    }

    @Query(value = """
            SELECT m.id as messageId,
                   m.content as content,
                   m.created_at as createdAt,
                   u.id as authorId,
                   u.full_name as authorFullName,
                   u.username as authorUsername,
                   COALESCE(total_votes.upvote_count, 0) as upvoteCount,
                   COALESCE(total_votes.downvote_count, 0) as downvoteCount,
                   COALESCE(total_votes.upvote_count, 0) - COALESCE(total_votes.downvote_count, 0) as score,
                   COALESCE(recent_votes.recent_vote_count, 0) as recentVotes
            FROM messages m
            JOIN users u ON m.user_id = u.id
            LEFT JOIN (
                SELECT message_id,
                       SUM(CASE WHEN vote_type = 'UP' THEN 1 ELSE 0 END) as upvote_count,
                       SUM(CASE WHEN vote_type = 'DOWN' THEN 1 ELSE 0 END) as downvote_count
                FROM votes
                GROUP BY message_id
            ) total_votes ON m.id = total_votes.message_id
            LEFT JOIN (
                SELECT message_id, COUNT(*) as recent_vote_count
                FROM votes
                WHERE created_at >= :startDate
                GROUP BY message_id
            ) recent_votes ON m.id = recent_votes.message_id
            WHERE m.status = 'PUBLISHED'
            ORDER BY recentVotes DESC, score DESC, m.created_at DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<TrendingMessageStats> getTrendingMessages(@Param("startDate") Instant startDate, @Param("limit") int limit);

    interface TrendingMessageStats {
        Long getMessageId();

        String getContent();

        Instant getCreatedAt();

        Long getAuthorId();

        String getAuthorFullName();

        String getAuthorUsername();

        Long getUpvoteCount();

        Long getDownvoteCount();

        Long getScore();

        Long getRecentVotes();
    }
}
