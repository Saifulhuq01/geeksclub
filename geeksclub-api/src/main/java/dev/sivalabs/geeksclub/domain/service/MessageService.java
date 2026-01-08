package dev.sivalabs.geeksclub.domain.service;

import dev.sivalabs.geeksclub.domain.dto.CreateMessageCmd;
import dev.sivalabs.geeksclub.domain.dto.MessageDetailVM;
import dev.sivalabs.geeksclub.domain.dto.MessageFeedItemVM;
import dev.sivalabs.geeksclub.domain.dto.MessageVM;
import dev.sivalabs.geeksclub.domain.dto.RemoveVoteResult;
import dev.sivalabs.geeksclub.domain.dto.SortBy;
import dev.sivalabs.geeksclub.domain.dto.UserVoteResult;
import dev.sivalabs.geeksclub.domain.dto.VoteResult;
import dev.sivalabs.geeksclub.domain.dto.VoteType;
import dev.sivalabs.geeksclub.domain.entity.BaseEntity;
import dev.sivalabs.geeksclub.domain.entity.MessageEntity;
import dev.sivalabs.geeksclub.domain.entity.UserEntity;
import dev.sivalabs.geeksclub.domain.entity.VoteEntity;
import dev.sivalabs.geeksclub.domain.exception.ConflictException;
import dev.sivalabs.geeksclub.domain.exception.ResourceNotFoundException;
import dev.sivalabs.geeksclub.domain.exception.UserNotFoundException;
import dev.sivalabs.geeksclub.domain.repo.MessageRepository;
import dev.sivalabs.geeksclub.domain.repo.UserRepository;
import dev.sivalabs.geeksclub.domain.repo.VoteRepository;
import dev.sivalabs.geeksclub.domain.utils.IdGenerator;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageEntityMapper messageEntityMapper;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    MessageService(
            MessageRepository messageRepository,
            MessageEntityMapper messageEntityMapper,
            VoteRepository voteRepository,
            UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.messageEntityMapper = messageEntityMapper;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public MessageVM createMessage(CreateMessageCmd cmd) {
        var message = new MessageEntity(IdGenerator.generateLong(), cmd.userId(), cmd.content());
        var savedMessage = messageRepository.save(message);
        return messageEntityMapper.toMessageVM(savedMessage);
    }

    public Page<MessageFeedItemVM> getMessageFeed(int page, int size, SortBy sortBy, Long currentUserId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MessageEntity> messagePage =
                switch (sortBy) {
                    case RECENT -> messageRepository.findAllPublishedOrderByCreatedAtDesc(pageable);
                    case UPVOTED -> messageRepository.findAllPublishedOrderByUpvotesDesc(pageable);
                    case DOWNVOTED -> messageRepository.findAllPublishedOrderByDownvotesDesc(pageable);
                    case TRENDING -> messageRepository.findAllPublishedOrderByTrending(pageable);
                };

        if (messagePage.isEmpty()) {
            return messagePage.map(m -> null);
        }

        List<Long> messageIds =
                messagePage.getContent().stream().map(MessageEntity::getId).collect(Collectors.toList());

        // Get vote counts for all messages
        Map<Long, VoteCounts> voteCountsMap = getVoteCountsMap(messageIds);

        // Get user votes if authenticated
        Map<Long, VoteType> userVotesMap = new HashMap<>();
        if (currentUserId != null) {
            userVotesMap = getUserVotesMap(messageIds, currentUserId);
        }

        // Get user info for all message authors
        Set<Long> userIds =
                messagePage.getContent().stream().map(MessageEntity::getUserId).collect(Collectors.toSet());
        Map<Long, UserInfo> userInfoMap = getUserInfoMap(userIds);

        Map<Long, VoteType> finalUserVotesMap = userVotesMap;
        return messagePage.map(message -> {
            VoteCounts voteCounts = voteCountsMap.getOrDefault(message.getId(), new VoteCounts(0, 0));
            VoteType userVote = finalUserVotesMap.get(message.getId());
            UserInfo userInfo = userInfoMap.get(message.getUserId());

            return new MessageFeedItemVM(
                    message.getId(),
                    message.getContent(),
                    userInfo != null ? userInfo.id() : null,
                    userInfo != null ? userInfo.fullName() : null,
                    userInfo != null ? userInfo.username() : null,
                    message.getStatus(),
                    message.isSpam(),
                    voteCounts.upvoteCount(),
                    voteCounts.downvoteCount(),
                    voteCounts.upvoteCount() - voteCounts.downvoteCount(),
                    userVote != null ? userVote.name() : null,
                    message.getCreatedAt());
        });
    }

    public Page<MessageFeedItemVM> getUserMessages(String username, int page, int size, Long currentUserId) {
        // Verify user exists
        userRepository
                .findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        Pageable pageable = PageRequest.of(page, size);
        Page<MessageEntity> messagePage = messageRepository.findByUsernameOrderByCreatedAtDesc(username, pageable);

        if (messagePage.isEmpty()) {
            return messagePage.map(m -> null);
        }

        List<Long> messageIds =
                messagePage.getContent().stream().map(MessageEntity::getId).collect(Collectors.toList());

        // Get vote counts for all messages
        Map<Long, VoteCounts> voteCountsMap = getVoteCountsMap(messageIds);

        // Get user votes if authenticated
        Map<Long, VoteType> userVotesMap = new HashMap<>();
        if (currentUserId != null) {
            userVotesMap = getUserVotesMap(messageIds, currentUserId);
        }

        // Get user info for all message authors
        Set<Long> userIds =
                messagePage.getContent().stream().map(MessageEntity::getUserId).collect(Collectors.toSet());
        Map<Long, UserInfo> userInfoMap = getUserInfoMap(userIds);

        Map<Long, VoteType> finalUserVotesMap = userVotesMap;
        return messagePage.map(message -> {
            VoteCounts voteCounts = voteCountsMap.getOrDefault(message.getId(), new VoteCounts(0, 0));
            VoteType userVote = finalUserVotesMap.get(message.getId());
            UserInfo userInfo = userInfoMap.get(message.getUserId());

            return new MessageFeedItemVM(
                    message.getId(),
                    message.getContent(),
                    userInfo != null ? userInfo.id() : null,
                    userInfo != null ? userInfo.fullName() : null,
                    userInfo != null ? userInfo.username() : null,
                    message.getStatus(),
                    message.isSpam(),
                    voteCounts.upvoteCount(),
                    voteCounts.downvoteCount(),
                    voteCounts.upvoteCount() - voteCounts.downvoteCount(),
                    userVote != null ? userVote.name() : null,
                    message.getCreatedAt());
        });
    }

    @Transactional
    public void deleteMessage(Long messageId, Long currentUserId, boolean isAdmin) {
        MessageEntity message = messageRepository
                .findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + messageId));

        // Check if user is authorized to delete (must be author or admin)
        if (!isAdmin && !message.getUserId().equals(currentUserId)) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "You are not authorized to delete this message");
        }

        messageRepository.delete(message);
    }

    public MessageDetailVM getMessage(Long messageId, Long currentUserId) {
        MessageEntity message = messageRepository
                .findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + messageId));

        // Get vote counts for this message
        List<VoteRepository.VoteCount> voteCounts = voteRepository.getVoteCountsByMessageIds(List.of(messageId));
        VoteCounts counts = voteCounts.isEmpty()
                ? new VoteCounts(0, 0)
                : new VoteCounts(
                        voteCounts.get(0).getUpvoteCount().intValue(),
                        voteCounts.get(0).getDownvoteCount().intValue());

        // Get user vote if authenticated
        VoteType userVote = null;
        if (currentUserId != null) {
            Optional<VoteEntity> vote = voteRepository.findByMessageIdAndUserId(messageId, currentUserId);
            userVote = vote.map(VoteEntity::getVoteType).orElse(null);
        }

        // Get author information
        UserEntity author = userRepository
                .findById(message.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + message.getUserId()));

        return new MessageDetailVM(
                message.getId(),
                message.getContent(),
                author.getId(),
                author.getUsername(),
                author.getFullName(),
                message.getStatus(),
                message.isSpam(),
                message.getSpamConfidence(),
                counts.upvoteCount(),
                counts.downvoteCount(),
                counts.upvoteCount() - counts.downvoteCount(),
                userVote != null ? userVote.name() : null,
                message.getCreatedAt(),
                message.getUpdatedAt());
    }

    @Transactional
    public VoteResult vote(Long messageId, Long userId, VoteType voteType) {
        userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        MessageEntity message = messageRepository
                .findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message with id " + messageId + " not found"));

        if (message.getUserId().equals(userId)) {
            throw new ConflictException("You cannot vote for your own message");
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

        int upvoteCount = voteRepository.countUpVotes(messageId);
        int downvoteCount = voteRepository.countDownVotes(messageId);
        int score = upvoteCount - downvoteCount;

        return new VoteResult(messageId, voteType, upvoteCount, downvoteCount, score, vote.getUpdatedAt());
    }

    public UserVoteResult getUserVote(Long messageId, Long userId) {
        messageRepository
                .findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message with id " + messageId + " not found"));

        return voteRepository
                .findByMessageIdAndUserId(messageId, userId)
                .map(v -> new UserVoteResult(messageId, v.getVoteType(), v.getUpdatedAt()))
                .orElse(new UserVoteResult(messageId, null, null));
    }

    @Transactional
    public RemoveVoteResult removeVote(Long messageId, Long userId) {
        messageRepository
                .findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message with id " + messageId + " not found"));

        VoteEntity vote = voteRepository
                .findByMessageIdAndUserId(messageId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vote not found for message " + messageId + " by user " + userId));

        voteRepository.delete(vote);

        int upvoteCount = voteRepository.countUpVotes(messageId);
        int downvoteCount = voteRepository.countDownVotes(messageId);
        int score = upvoteCount - downvoteCount;

        return new RemoveVoteResult(messageId, upvoteCount, downvoteCount, score, "Vote removed successfully");
    }

    private Map<Long, VoteCounts> getVoteCountsMap(List<Long> messageIds) {
        List<VoteRepository.VoteCount> voteCounts = voteRepository.getVoteCountsByMessageIds(messageIds);
        return voteCounts.stream()
                .collect(Collectors.toMap(
                        VoteRepository.VoteCount::getMessageId,
                        vc -> new VoteCounts(
                                vc.getUpvoteCount().intValue(),
                                vc.getDownvoteCount().intValue())));
    }

    private Map<Long, VoteType> getUserVotesMap(List<Long> messageIds, Long userId) {
        List<VoteEntity> userVotes = voteRepository.findByMessageIdsAndUserId(messageIds, userId);
        return userVotes.stream().collect(Collectors.toMap(VoteEntity::getMessageId, VoteEntity::getVoteType));
    }

    private Map<Long, UserInfo> getUserInfoMap(Set<Long> userIds) {
        return userRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(
                        BaseEntity::getId, user -> new UserInfo(user.getId(), user.getFullName(), user.getUsername())));
    }

    record VoteCounts(int upvoteCount, int downvoteCount) {}

    record UserInfo(Long id, String fullName, String username) {}
}
