package dev.sivalabs.geeksclub.domain.service;

import dev.sivalabs.geeksclub.domain.dto.MessageVM;
import dev.sivalabs.geeksclub.domain.entity.MessageEntity;
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
