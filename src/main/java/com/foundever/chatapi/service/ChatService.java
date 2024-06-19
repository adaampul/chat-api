package com.foundever.chatapi.service;

import com.foundever.chatapi.mapper.ChatMessagesMapper;
import com.foundever.chatapi.model.entity.ChatMessage;
import com.foundever.chatapi.model.entity.SupportCase;
import com.foundever.chatapi.model.dto.ChatMessageDto;
import com.foundever.chatapi.repository.ChatMessagesRepository;
import com.foundever.chatapi.repository.SupportCasesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessagesRepository messagesRepository;
    private final SupportCasesRepository supportCasesRepository;
    private final ChatMessagesMapper mapper;
    private final Clock clock;

    @Transactional
    public ChatMessage addMessage(ChatMessageDto chatMessage) {
        var messageToSave = mapper.toChatMessage(chatMessage).toBuilder()
                        .timestamp(clock.instant())
                        .build();
        var message = messagesRepository.save(messageToSave);

        Optional.ofNullable(message.getSupportCase())
                .map(SupportCase::getId)
                .flatMap(supportCasesRepository::findById)
                .ifPresent(supportCase -> assignMessageToSupportCase(supportCase, message));
        return message;
    }

    void assignMessageToSupportCase(SupportCase supportCase, ChatMessage chatMessage) {
        chatMessage.setSupportCase(supportCase);
        var messages = new ArrayList<>(supportCase.getMessages());
        messages.add(chatMessage);
        supportCase.setMessages(messages);
    }

}
