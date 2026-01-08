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
}
