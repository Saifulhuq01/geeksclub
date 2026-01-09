package dev.sivalabs.geeksclub.domain.entity;

import dev.sivalabs.geeksclub.domain.dto.MessageStatus;
import dev.sivalabs.geeksclub.domain.utils.AssertUtil;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "messages")
public class MessageEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_spam", nullable = false)
    private boolean isSpam = false;

    @Column(name = "spam_confidence", precision = 5, scale = 4)
    private BigDecimal spamConfidence;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private MessageStatus status = MessageStatus.PUBLISHED;

    @Column(name = "reviewed_by")
    private String reviewedBy;

    @Column(name = "reviewed_at")
    private Instant reviewedAt;

    @Column(name = "review_notes", columnDefinition = "TEXT")
    private String reviewNotes;

    @Version
    private int version;

    protected MessageEntity() {}

    public MessageEntity(Long id, Long userId, String content) {
        this.id = AssertUtil.requireNotNull(id, "Message ID cannot be null");
        this.userId = AssertUtil.requireNotNull(userId, "User ID cannot be null");
        this.content = AssertUtil.requireNotNull(content, "Content cannot be null");
        this.status = MessageStatus.PUBLISHED;
        this.isSpam = false;
    }

    public Long getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public boolean isSpam() {
        return isSpam;
    }

    public BigDecimal getSpamConfidence() {
        return spamConfidence;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setSpam(boolean spam, BigDecimal spamConfidence) {
        this.isSpam = spam;
        this.spamConfidence = spamConfidence;
    }

    public void updateStatus(MessageStatus status) {
        this.status = status;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public Instant getReviewedAt() {
        return reviewedAt;
    }

    public String getReviewNotes() {
        return reviewNotes;
    }

    public void setReview(String reviewedBy, Instant reviewedAt, String reviewNotes) {
        this.reviewedBy = reviewedBy;
        this.reviewedAt = reviewedAt;
        this.reviewNotes = reviewNotes;
    }
}
