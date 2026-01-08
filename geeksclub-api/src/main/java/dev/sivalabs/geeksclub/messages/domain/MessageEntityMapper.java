package dev.sivalabs.geeksclub.messages.domain;

import dev.sivalabs.geeksclub.messages.domain.dto.MessageVM;
import org.springframework.stereotype.Component;

@Component
class MessageEntityMapper {

    public MessageVM toMessageVM(MessageEntity messageEntity) {
        return new MessageVM(
                messageEntity.getId(),
                messageEntity.getUserId(),
                messageEntity.getContent(),
                messageEntity.getStatus(),
                messageEntity.isSpam(),
                messageEntity.getSpamConfidence(),
                messageEntity.getCreatedAt(),
                messageEntity.getUpdatedAt());
    }
}
