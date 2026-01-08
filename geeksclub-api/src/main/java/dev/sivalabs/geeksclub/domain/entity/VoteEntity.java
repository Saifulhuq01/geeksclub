package dev.sivalabs.geeksclub.domain.entity;

import dev.sivalabs.geeksclub.domain.dto.VoteType;
import dev.sivalabs.geeksclub.domain.utils.AssertUtil;
import jakarta.persistence.*;

@Entity
@Table(name = "votes")
public class VoteEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "message_id", nullable = false)
    private Long messageId;

    @Column(name = "vote_type", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private VoteType voteType;

    @Version
    private int version;

    protected VoteEntity() {}

    public VoteEntity(Long id, Long userId, Long messageId, VoteType voteType) {
        this.id = AssertUtil.requireNotNull(id, "Vote ID cannot be null");
        this.userId = AssertUtil.requireNotNull(userId, "User ID cannot be null");
        this.messageId = AssertUtil.requireNotNull(messageId, "Message ID cannot be null");
        this.voteType = AssertUtil.requireNotNull(voteType, "Vote type cannot be null");
    }

    public Long getUserId() {
        return userId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public void updateVoteType(VoteType voteType) {
        this.voteType = voteType;
    }
}
