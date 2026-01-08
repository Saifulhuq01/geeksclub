package dev.sivalabs.geeksclub.messages.domain;

import dev.sivalabs.geeksclub.messages.domain.dto.CreateMessageCmd;
import dev.sivalabs.geeksclub.messages.domain.dto.MessageVM;
import dev.sivalabs.geeksclub.shared.utils.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageEntityMapper messageEntityMapper;

    MessageService(MessageRepository messageRepository, MessageEntityMapper messageEntityMapper) {
        this.messageRepository = messageRepository;
        this.messageEntityMapper = messageEntityMapper;
    }

    @Transactional
    public MessageVM createMessage(CreateMessageCmd cmd) {
        var message = new MessageEntity(IdGenerator.generateLong(), cmd.userId(), cmd.content());
        var savedMessage = messageRepository.save(message);
        return messageEntityMapper.toMessageVM(savedMessage);
    }
}
