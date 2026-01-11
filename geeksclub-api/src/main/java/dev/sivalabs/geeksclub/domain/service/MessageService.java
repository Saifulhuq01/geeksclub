package dev.sivalabs.geeksclub.domain.service;

import dev.sivalabs.geeksclub.domain.dto.*;
import dev.sivalabs.geeksclub.domain.entity.MessageEntity;
import dev.sivalabs.geeksclub.domain.exception.*;
import dev.sivalabs.geeksclub.domain.repo.MessageRepository;
import dev.sivalabs.geeksclub.domain.repo.MessageRepository.MessageDetailsProjection;
import dev.sivalabs.geeksclub.domain.repo.UserRepository;
import dev.sivalabs.geeksclub.domain.utils.IdGenerator;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MessageService {
    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    private final MessageRepository messageRepository;
    private final MessageEntityMapper messageEntityMapper;
    private final UserRepository userRepository;
    private final MessageContentValidator messageContentValidator;

    MessageService(
            MessageRepository messageRepository,
            MessageEntityMapper messageEntityMapper,
            UserRepository userRepository,
            MessageContentValidator messageContentValidator) {
        this.messageRepository = messageRepository;
        this.messageEntityMapper = messageEntityMapper;
        this.userRepository = userRepository;
        this.messageContentValidator = messageContentValidator;
    }

    @Transactional
    public MessageVM createMessage(CreateMessageCmd cmd) {
        log.info("Creating message for user {}", cmd.userId());
        messageContentValidator.validate(cmd.content());
        var message = new MessageEntity(IdGenerator.generateLong(), cmd.userId(), cmd.content());
        var savedMessage = messageRepository.save(message);
        log.info("Message {} created successfully by user {}", savedMessage.getId(), cmd.userId());
        return messageEntityMapper.toMessageVM(savedMessage);
    }

    public Page<MessageDetailVM> getMessageFeed(int page, int size, SortBy sortBy, Long currentUserId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MessageEntity> messagePage =
                switch (sortBy) {
                    case RECENT -> messageRepository.findAllPublishedOrderByCreatedAtDesc(pageable);
                    case UPVOTED -> messageRepository.findAllPublishedOrderByUpvotesDesc(pageable);
                    case DOWNVOTED -> messageRepository.findAllPublishedOrderByDownvotesDesc(pageable);
                    case TRENDING -> messageRepository.findAllPublishedOrderByTrending(pageable);
                };

        if (messagePage.isEmpty()) {
            return Page.empty();
        }

        return getMessageFeedItemVMS(currentUserId, messagePage);
    }

    public Page<MessageDetailVM> getUserMessages(String username, int page, int size, Long currentUserId) {
        // Verify user exists
        userRepository
                .findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        Pageable pageable = PageRequest.of(page, size);
        Page<MessageEntity> messagePage = messageRepository.findByUsernameOrderByCreatedAtDesc(username, pageable);

        if (messagePage.isEmpty()) {
            return Page.empty();
        }

        return getMessageFeedItemVMS(currentUserId, messagePage);
    }

    public Page<MessageDetailVM> searchMessages(String query, int page, int size, Long currentUserId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MessageEntity> messagePage = messageRepository.searchMessages(query, pageable);

        if (messagePage.isEmpty()) {
            return Page.empty();
        }

        return getMessageFeedItemVMS(currentUserId, messagePage);
    }

    private Page<MessageDetailVM> getMessageFeedItemVMS(Long currentUserId, Page<MessageEntity> messagePage) {
        List<Long> messageIds =
                messagePage.getContent().stream().map(MessageEntity::getId).toList();
        List<MessageDetailsProjection> messagesDetails =
                messageRepository.findMessageDetails(messageIds, currentUserId);
        Map<Long, MessageDetailsProjection> messageDetailsMap = new HashMap<>();
        for (MessageDetailsProjection projection : messagesDetails) {
            messageDetailsMap.put(projection.getId(), projection);
        }

        return messagePage.map(message -> {
            var messageDetails = messageDetailsMap.get(message.getId());
            return buildMessageDetailVM(message, messageDetails);
        });
    }

    public MessageDetailVM getMessage(Long messageId, Long currentUserId) {
        var message = messageRepository.findById(messageId).orElseThrow(() -> messageNotFoundException(messageId));
        var messageDetails = messageRepository
                .findMessageDetails(List.of(messageId), currentUserId)
                .getFirst();
        return buildMessageDetailVM(message, messageDetails);
    }

    private MessageDetailVM buildMessageDetailVM(MessageEntity message, MessageDetailsProjection messageDetails) {
        return new MessageDetailVM(
                message.getId(),
                message.getContent(),
                messageDetails.getAuthorId(),
                messageDetails.getAuthorUsername(),
                messageDetails.getAuthorFullName(),
                message.getStatus(),
                message.isSpam(),
                message.getSpamConfidence(),
                messageDetails.getUpvotes(),
                messageDetails.getDownvotes(),
                messageDetails.getUpvotes() - messageDetails.getDownvotes(),
                messageDetails.getUserVote(),
                message.getCreatedAt(),
                message.getUpdatedAt());
    }

    @Transactional
    public void deleteMessage(Long messageId, Long currentUserId, boolean isAdmin) {
        log.info("Delete request for message {} by user {} (admin: {})", messageId, currentUserId, isAdmin);
        var message = messageRepository.findById(messageId).orElseThrow(() -> messageNotFoundException(messageId));

        // Check if user is authorized to delete (must be author or admin)
        if (!isAdmin && !message.getUserId().equals(currentUserId)) {
            log.warn("Unauthorized delete attempt for message {} by user {}", messageId, currentUserId);
            throw new UnauthorizedOperationException("You are not authorized to delete this message");
        }
        messageRepository.delete(message);
        log.info("Message {} deleted successfully", messageId);
    }

    private MessageNotFoundException messageNotFoundException(Long messageId) {
        return new MessageNotFoundException("Message with id " + messageId + " not found");
    }

    @Transactional
    public ReviewMessageResult reviewMessage(Long messageId, String action, String reviewedBy, String notes) {
        MessageEntity message =
                messageRepository.findById(messageId).orElseThrow(() -> messageNotFoundException(messageId));

        Instant reviewedAt = Instant.now();

        if ("APPROVE".equals(action)) {
            message.updateStatus(MessageStatus.PUBLISHED);
            message.setSpam(false, BigDecimal.ZERO);
        } else if ("REMOVE".equals(action)) {
            message.updateStatus(MessageStatus.REMOVED);
        }

        message.setReview(reviewedBy, reviewedAt, notes);
        messageRepository.save(message);

        return new ReviewMessageResult(message.getId(), message.getStatus(), reviewedBy, reviewedAt, notes);
    }
}
