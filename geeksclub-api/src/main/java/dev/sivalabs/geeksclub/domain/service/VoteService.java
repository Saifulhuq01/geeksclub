package dev.sivalabs.geeksclub.domain.service;

import dev.sivalabs.geeksclub.domain.dto.*;
import dev.sivalabs.geeksclub.domain.entity.VoteEntity;
import dev.sivalabs.geeksclub.domain.exception.BadRequestException;
import dev.sivalabs.geeksclub.domain.exception.MessageNotFoundException;
import dev.sivalabs.geeksclub.domain.exception.VoteNotFoundException;
import dev.sivalabs.geeksclub.domain.repo.MessageRepository;
import dev.sivalabs.geeksclub.domain.repo.VoteRepository;
import dev.sivalabs.geeksclub.domain.utils.IdGenerator;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VoteService {
    private final MessageRepository messageRepository;
    private final VoteRepository voteRepository;

    VoteService(MessageRepository messageRepository, VoteRepository voteRepository) {
        this.messageRepository = messageRepository;
        this.voteRepository = voteRepository;
    }

    @Transactional
    public VoteResult vote(Long messageId, Long userId, VoteType voteType) {
        var message = messageRepository.findById(messageId).orElseThrow(() -> messageNotFoundException(messageId));

        if (message.getUserId().equals(userId)) {
            throw new BadRequestException("You cannot vote for your own message");
        }

        Optional<VoteEntity> existingVoteOptional = voteRepository.findByMessageIdAndUserId(messageId, userId);

        VoteEntity vote;
        if (existingVoteOptional.isPresent()) {
            vote = existingVoteOptional.get();
            if (vote.getVoteType() != voteType) {
                vote.updateVoteType(voteType);
                vote = voteRepository.save(vote);
            }
        } else {
            vote = new VoteEntity(IdGenerator.generateLong(), userId, messageId, voteType);
            vote = voteRepository.save(vote);
        }

        VoteCounts counts = voteRepository.getVoteCounts(messageId).orElse(new VoteCounts(0, 0));
        return new VoteResult(
                messageId, voteType, counts.upvotes(), counts.downvotes(), counts.score(), vote.getUpdatedAt());
    }

    public UserVoteResult getUserVote(Long messageId, Long userId) {
        messageRepository.findById(messageId).orElseThrow(() -> messageNotFoundException(messageId));

        return voteRepository
                .findByMessageIdAndUserId(messageId, userId)
                .map(v -> new UserVoteResult(messageId, v.getVoteType(), v.getUpdatedAt()))
                .orElse(new UserVoteResult(messageId, null, null));
    }

    @Transactional
    public RemoveVoteResult removeVote(Long messageId, Long userId) {
        messageRepository.findById(messageId).orElseThrow(() -> messageNotFoundException(messageId));

        VoteEntity vote = voteRepository
                .findByMessageIdAndUserId(messageId, userId)
                .orElseThrow(() ->
                        new VoteNotFoundException("Vote not found for message " + messageId + " by user " + userId));

        voteRepository.delete(vote);

        VoteCounts counts = voteRepository.getVoteCounts(messageId).orElse(new VoteCounts(0, 0));

        return new RemoveVoteResult(
                messageId, counts.upvotes(), counts.downvotes(), counts.score(), "Vote removed successfully");
    }

    private MessageNotFoundException messageNotFoundException(Long messageId) {
        return new MessageNotFoundException("Message with id " + messageId + " not found");
    }
}
