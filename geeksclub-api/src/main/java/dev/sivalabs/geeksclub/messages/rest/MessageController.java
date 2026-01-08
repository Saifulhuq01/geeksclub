package dev.sivalabs.geeksclub.messages.rest;

import static org.springframework.http.HttpStatus.CREATED;

import dev.sivalabs.geeksclub.messages.domain.MessageService;
import dev.sivalabs.geeksclub.messages.domain.SortBy;
import dev.sivalabs.geeksclub.messages.domain.dto.CreateMessageCmd;
import dev.sivalabs.geeksclub.messages.domain.dto.MessageDetailVM;
import dev.sivalabs.geeksclub.messages.domain.dto.MessageFeedItemVM;
import dev.sivalabs.geeksclub.messages.rest.dto.CreateMessageRequest;
import dev.sivalabs.geeksclub.messages.rest.dto.CreateMessageResponse;
import dev.sivalabs.geeksclub.messages.rest.dto.MessageDetailResponse;
import dev.sivalabs.geeksclub.messages.rest.dto.MessageFeedItem;
import dev.sivalabs.geeksclub.users.UserContextUtils;
import dev.sivalabs.geeksclub.users.domain.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "recent") String sort) {

        // Validate and limit page size
        if (size > 100) {
            size = 100;
        }

        SortBy sortBy = SortBy.fromString(sort);
        Long currentUserId = userContextUtils.getCurrentUserId();

        Page<MessageFeedItemVM> feedPage = messageService.getMessageFeed(page, size, sortBy, currentUserId);

        Page<MessageFeedItem> response = feedPage.map(item -> new MessageFeedItem(
                item.id(),
                item.content(),
                new MessageFeedItem.AuthorInfo(item.authorId(), item.authorUsername()),
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
}
