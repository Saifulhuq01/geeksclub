package dev.sivalabs.geeksclub.messages.domain;

import dev.sivalabs.geeksclub.shared.entity.BaseEntity;
import dev.sivalabs.geeksclub.shared.utils.AssertUtil;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "messages")
class MessageEntity extends BaseEntity {

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
}
