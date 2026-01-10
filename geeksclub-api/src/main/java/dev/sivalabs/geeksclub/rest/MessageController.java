package dev.sivalabs.geeksclub.rest;

import static org.springframework.http.HttpStatus.CREATED;

import dev.sivalabs.geeksclub.domain.dto.CreateMessageCmd;
import dev.sivalabs.geeksclub.domain.dto.MessageDetailVM;
import dev.sivalabs.geeksclub.domain.dto.RemoveVoteResult;
import dev.sivalabs.geeksclub.domain.dto.SortBy;
import dev.sivalabs.geeksclub.domain.dto.UserVoteResult;
import dev.sivalabs.geeksclub.domain.dto.VoteResult;
import dev.sivalabs.geeksclub.domain.service.MessageService;
import dev.sivalabs.geeksclub.domain.service.UserService;
import dev.sivalabs.geeksclub.domain.service.VoteService;
import dev.sivalabs.geeksclub.rest.dto.CreateMessageRequest;
import dev.sivalabs.geeksclub.rest.dto.CreateMessageResponse;
import dev.sivalabs.geeksclub.rest.dto.MessageDetailResponse;
import dev.sivalabs.geeksclub.rest.dto.MessageFeedItem;
import dev.sivalabs.geeksclub.rest.dto.RemoveVoteResponse;
import dev.sivalabs.geeksclub.rest.dto.UserVoteResponse;
import dev.sivalabs.geeksclub.rest.dto.VoteRequest;
import dev.sivalabs.geeksclub.rest.dto.VoteResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages API")
class MessageController {
    private final MessageService messageService;
    private final VoteService voteService;
    private final UserService userService;
    private final UserContextUtils userContextUtils;

    MessageController(
            MessageService messageService,
            VoteService voteService,
            UserService userService,
            UserContextUtils userContextUtils) {
        this.messageService = messageService;
        this.voteService = voteService;
        this.userService = userService;
        this.userContextUtils = userContextUtils;
    }

    @PostMapping("")
    @SecurityRequirement(name = "Bearer")
    ResponseEntity<CreateMessageResponse> createMessage(@RequestBody @Valid CreateMessageRequest request) {
        var currentUser = userContextUtils.getCurrentUserOrThrow();
        var cmd = new CreateMessageCmd(currentUser.id(), request.content());
        var message = messageService.createMessage(cmd);

        var author = userService.getByUsername(currentUser.username());
        var authorInfo = new CreateMessageResponse.AuthorInfo(author.id(), author.fullName(), author.username());
        var votesInfo = new CreateMessageResponse.VotesInfo(0, 0, 0);

        var response = new CreateMessageResponse(
                message.id(),
                message.content(),
                authorInfo,
                message.status(),
                message.isSpam(),
                votesInfo,
                null,
                message.createdAt(),
                message.updatedAt());

        return ResponseEntity.status(CREATED.value()).body(response);
    }

    @GetMapping("")
    ResponseEntity<Page<MessageFeedItem>> getMessageFeed(
            @RequestParam(required = false) String user,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Max(100) int size,
            @RequestParam(defaultValue = "recent") String sort) {

        Long currentUserId = userContextUtils.getCurrentUserId();
        Page<MessageDetailVM> feedPage;

        if (user != null && !user.isBlank()) {
            // Get messages by specific user
            feedPage = messageService.getUserMessages(user, page, size, currentUserId);
        } else {
            // Get general message feed
            SortBy sortBy = SortBy.fromString(sort);
            feedPage = messageService.getMessageFeed(page, size, sortBy, currentUserId);
        }

        return mapToMessageFeedItems(feedPage);
    }

    @GetMapping("/search")
    ResponseEntity<Page<MessageFeedItem>> searchMessages(
            @RequestParam @Size(min = 3, max = 100, message = "Query must be between 3 and 100 characters") String q,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Max(100) int size) {

        // Sanitize input - escape LIKE wildcards
        String sanitizedQuery = q.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");

        Long currentUserId = userContextUtils.getCurrentUserId();
        Page<MessageDetailVM> feedPage = messageService.searchMessages(sanitizedQuery, page, size, currentUserId);

        return mapToMessageFeedItems(feedPage);
    }

    @GetMapping("/{id}")
    ResponseEntity<MessageDetailResponse> getMessageById(@PathVariable Long id) {
        Long currentUserId = userContextUtils.getCurrentUserId();
        MessageDetailVM message = messageService.getMessage(id, currentUserId);

        var response = new MessageDetailResponse(
                message.id(),
                message.content(),
                new MessageDetailResponse.AuthorInfo(
                        message.authorId(), message.authorFullName(), message.authorUsername()),
                message.status(),
                message.isSpam(),
                message.spamConfidence(),
                new MessageDetailResponse.VotesInfo(message.upvoteCount(), message.downvoteCount(), message.score()),
                message.userVote(),
                message.createdAt(),
                message.updatedAt());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        var currentUserId = userContextUtils.getCurrentUserIdOrThrow();
        boolean isAdmin = userContextUtils.isCurrentUserAdmin();
        messageService.deleteMessage(id, currentUserId, isAdmin);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{messageId}/vote")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<UserVoteResponse> getUserVote(@PathVariable Long messageId) {
        var currentUserId = userContextUtils.getCurrentUserIdOrThrow();
        UserVoteResult result = voteService.getUserVote(messageId, currentUserId);
        UserVoteResponse response = new UserVoteResponse(result.messageId(), result.voteType(), result.votedAt());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{messageId}/vote")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<VoteResponse> vote(@PathVariable Long messageId, @RequestBody @Valid VoteRequest request) {
        var userId = userContextUtils.getCurrentUserIdOrThrow();
        VoteResult result = voteService.vote(messageId, userId, request.voteType());
        VoteResponse response = new VoteResponse(
                result.messageId(),
                result.voteType(),
                new VoteResponse.Votes(result.upvoteCount(), result.downvoteCount(), result.score()),
                result.votedAt());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{messageId}/vote")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<RemoveVoteResponse> removeVote(@PathVariable Long messageId) {
        var userId = userContextUtils.getCurrentUserIdOrThrow();
        RemoveVoteResult result = voteService.removeVote(messageId, userId);
        RemoveVoteResponse response = new RemoveVoteResponse(
                result.messageId(),
                new VoteResponse.Votes(result.upvoteCount(), result.downvoteCount(), result.score()),
                result.message());
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Page<MessageFeedItem>> mapToMessageFeedItems(Page<MessageDetailVM> feedPage) {
        Page<MessageFeedItem> response = feedPage.map(item -> new MessageFeedItem(
                item.id(),
                item.content(),
                new MessageFeedItem.AuthorInfo(item.authorId(), item.authorFullName(), item.authorUsername()),
                item.status(),
                item.isSpam(),
                new MessageFeedItem.VotesInfo(item.upvoteCount(), item.downvoteCount(), item.score()),
                item.userVote(),
                item.createdAt()));

        return ResponseEntity.ok(response);
    }
}
