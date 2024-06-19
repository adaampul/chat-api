package com.foundever.chatapi.service;

import com.foundever.chatapi.exception.ChatApiException;
import com.foundever.chatapi.model.entity.ChatMessage;
import com.foundever.chatapi.model.entity.SupportCase;
import com.foundever.chatapi.model.dto.SupportCaseDto;
import com.foundever.chatapi.repository.ChatMessagesRepository;
import com.foundever.chatapi.repository.SupportCasesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportCaseService {

    private final ChatMessagesRepository chatMessagesRepository;
    private final SupportCasesRepository supportCasesRepository;

    @Transactional
    public SupportCase createSupportCase(SupportCase supportCase) {
        var chatMessageIds = supportCase.getMessages().stream()
                .map(ChatMessage::getId)
                .toList();

        if (chatMessageIds.isEmpty()) {
            throw new ChatApiException(HttpStatusCode.valueOf(400), "At least one message is required to create a support case.");
        }

        var chatMessages = chatMessagesRepository.findAllById(chatMessageIds);
        var conflictingMessages = getConflictingMessages(chatMessages);

        if (!conflictingMessages.isEmpty()) {
            throw new ChatApiException(HttpStatusCode.valueOf(400), String.format(
                    "The following messages are already linked to existing support cases: %s", conflictingMessages));
        }
        supportCase.setMessages(chatMessages);
        var savedSupportCase = supportCasesRepository.save(supportCase);
        chatMessages.forEach(chatMessage -> chatMessage.setSupportCase(savedSupportCase));
        return savedSupportCase;
    }

    public List<SupportCase> getAll(Pageable pageable) {
        return supportCasesRepository.findAll(pageable).getContent();
    }

    @Transactional
    public SupportCase patchSupportCase(Long id, SupportCaseDto supportCaseData) {
        var supportCase = supportCasesRepository.findById(id)
                .orElseThrow(() -> new ChatApiException(HttpStatusCode.valueOf(404), String.format("Support case with given id: '%s' does not exist", id)));
        supportCase.setClientReference(supportCaseData.clientReference());
        return supportCase;
    }

    private Map<Long, List<Long>> getConflictingMessages(List<ChatMessage> chatMessages) {
        return chatMessages.stream()
                .filter(chatMessage -> nonNull(chatMessage.getSupportCase()))
                .collect(groupingBy(
                        chatMessage -> chatMessage.getSupportCase().getId(),
                        mapping(ChatMessage::getId, toList())
                ));
    }
}
