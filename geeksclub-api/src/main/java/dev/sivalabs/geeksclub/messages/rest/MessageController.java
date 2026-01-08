package dev.sivalabs.geeksclub.messages.rest;

import static org.springframework.http.HttpStatus.CREATED;

import dev.sivalabs.geeksclub.messages.domain.MessageService;
import dev.sivalabs.geeksclub.messages.domain.dto.CreateMessageCmd;
import dev.sivalabs.geeksclub.messages.rest.dto.CreateMessageRequest;
import dev.sivalabs.geeksclub.messages.rest.dto.CreateMessageResponse;
import dev.sivalabs.geeksclub.users.UserContextUtils;
import dev.sivalabs.geeksclub.users.domain.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages API")
@SecurityRequirement(name = "Bearer")
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
}
