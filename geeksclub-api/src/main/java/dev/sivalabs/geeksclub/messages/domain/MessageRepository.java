package dev.sivalabs.geeksclub.messages.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface MessageRepository extends JpaRepository<MessageEntity, Long> {

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
}
