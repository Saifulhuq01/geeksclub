package dev.sivalabs.geeksclub.domain.repo;

import dev.sivalabs.geeksclub.domain.entity.UserEntity;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByUsernameIgnoreCase(String username);

    Optional<UserEntity> findByUsernameIgnoreCase(String username);

    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.createdAt >= :since")
    long countUsersCreatedSince(@Param("since") Instant since);

    @Query(value = """
            SELECT DATE(activity_date) as date, COUNT(DISTINCT user_id) as activeUserCount
            FROM (
                SELECT created_at as activity_date, user_id FROM messages WHERE created_at >= :startDate
                UNION ALL
                SELECT created_at as activity_date, user_id FROM votes WHERE created_at >= :startDate
            ) activities
            GROUP BY DATE(activity_date)
            ORDER BY date DESC
            """, nativeQuery = true)
    List<DailyActiveUserStats> getDailyActiveUserStats(@Param("startDate") Instant startDate);

    interface DailyActiveUserStats {
        java.sql.Date getDate();

        long getActiveUserCount();
    }

    @Query(value = """
            SELECT u.id as userId,
                   u.full_name as fullName,
                   u.username as username,
                   u.email as email,
                   COALESCE(m.message_count, 0) as messageCount,
                   COALESCE(v.vote_count, 0) as voteCount,
                   COALESCE(m.message_count, 0) + COALESCE(v.vote_count, 0) as totalActivity,
                   CASE
                       WHEN m.last_activity > v.last_activity THEN m.last_activity
                       WHEN v.last_activity > m.last_activity THEN v.last_activity
                       WHEN m.last_activity IS NOT NULL THEN m.last_activity
                       WHEN v.last_activity IS NOT NULL THEN v.last_activity
                       ELSE u.created_at
                   END as lastActivityAt,
                   u.created_at as joinedAt
            FROM users u
            LEFT JOIN (
                SELECT user_id, COUNT(*) as message_count, MAX(created_at) as last_activity
                FROM messages
                GROUP BY user_id
            ) m ON u.id = m.user_id
            LEFT JOIN (
                SELECT user_id, COUNT(*) as vote_count, MAX(created_at) as last_activity
                FROM votes
                GROUP BY user_id
            ) v ON u.id = v.user_id
            ORDER BY totalActivity DESC, lastActivityAt DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<ActiveUserStats> getMostActiveUsers(@Param("limit") int limit);

    interface ActiveUserStats {
        Long getUserId();

        String getFullName();

        String getUsername();

        String getEmail();

        Long getMessageCount();

        Long getVoteCount();

        Long getTotalActivity();

        Instant getLastActivityAt();

        Instant getJoinedAt();
    }
}
