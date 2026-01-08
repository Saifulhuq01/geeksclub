package dev.sivalabs.geeksclub.rest;

import static org.springframework.http.HttpStatus.CREATED;

import dev.sivalabs.geeksclub.domain.dto.AuthenticatedUser;
import dev.sivalabs.geeksclub.domain.dto.CreateMessageCmd;
import dev.sivalabs.geeksclub.domain.dto.MessageDetailVM;
import dev.sivalabs.geeksclub.domain.dto.MessageFeedItemVM;
import dev.sivalabs.geeksclub.domain.dto.RemoveVoteResult;
import dev.sivalabs.geeksclub.domain.dto.SortBy;
import dev.sivalabs.geeksclub.domain.dto.UserVoteResult;
import dev.sivalabs.geeksclub.domain.dto.VoteResult;
import dev.sivalabs.geeksclub.domain.service.MessageService;
import dev.sivalabs.geeksclub.domain.service.UserService;
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
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages API")
class MessageController {
    private final MessageService messageService;
    private final UserService userService;
    private final UserContextUtils userContextUtils;

    MessageController(MessageService messageService, UserService userService, UserContextUtils userContextUtils) {
        this.messageService = messageService;
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "recent") String sort) {

        // Validate and limit page size
        if (size > 100) {
            size = 100;
        }

        Long currentUserId = userContextUtils.getCurrentUserId();
        Page<MessageFeedItemVM> feedPage;

        if (user != null && !user.isBlank()) {
            // Get messages by specific user
            feedPage = messageService.getUserMessages(user, page, size, currentUserId);
        } else {
            // Get general message feed
            SortBy sortBy = SortBy.fromString(sort);
            feedPage = messageService.getMessageFeed(page, size, sortBy, currentUserId);
        }

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
        var currentUser = userContextUtils.getCurrentUserOrThrow();
        boolean isAdmin = userContextUtils.isCurrentUserAdmin();
        messageService.deleteMessage(id, currentUser.id(), isAdmin);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{messageId}/vote")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<UserVoteResponse> getUserVote(@PathVariable Long messageId) {
        var user = userContextUtils.getCurrentUserOrThrow();
        UserVoteResult result = messageService.getUserVote(messageId, user.id());
        UserVoteResponse response = new UserVoteResponse(result.messageId(), result.voteType(), result.votedAt());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{messageId}/vote")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<VoteResponse> vote(@PathVariable Long messageId, @RequestBody @Valid VoteRequest request) {
        var user = userContextUtils.getCurrentUserOrThrow();
        VoteResult result = messageService.vote(messageId, user.id(), request.voteType());
        VoteResponse response = new VoteResponse(
                result.messageId(),
                result.voteType(),
                new VoteResponse.Votes(result.upvoteCount(), result.downvoteCount(), result.score()),
                result.votedAt());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{messageId}/vote")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<RemoveVoteResponse> removeVote(
            @PathVariable Long messageId, @AuthenticationPrincipal AuthenticatedUser user) {
        RemoveVoteResult result = messageService.removeVote(messageId, user.id());
        RemoveVoteResponse response = new RemoveVoteResponse(
                result.messageId(),
                new VoteResponse.Votes(result.upvoteCount(), result.downvoteCount(), result.score()),
                result.message());
        return ResponseEntity.ok(response);
    }
}
