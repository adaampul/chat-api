package com.foundever.chatapi.service;

import com.foundever.chatapi.mapper.ChatMessagesMapper;
import com.foundever.chatapi.model.entity.ChatMessage;
import com.foundever.chatapi.model.entity.SupportCase;
import com.foundever.chatapi.model.dto.ChatMessageDto;
import com.foundever.chatapi.repository.ChatMessagesRepository;
import com.foundever.chatapi.repository.SupportCasesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

    @Mock
    private ChatMessagesRepository messageRepository;

    @Mock
    private SupportCasesRepository supportCasesRepository;

    @Mock
    private ChatMessagesMapper mapper;

    @Mock
    private Clock clock;

    @InjectMocks
    private ChatService chatService;

    @Test
    void addMessage_supportCaseExists_supportCaseUpdated() {
        // Given
        Instant now = Instant.parse("2023-06-01T10:00:00Z");
        SupportCase supportCase = SupportCase.builder().id(456L).build();
        SupportCase supportCaseWithMessages = supportCase.toBuilder()
                .messages(List.of(
                        ChatMessage.builder().id(1L).build(),
                        ChatMessage.builder().id(2L).build(),
                        ChatMessage.builder().id(3L).build()))
                .build();

        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .userId(123L)
                .supportCaseId(456L)
                .content("message")
                .build();

        ChatMessage chatMessageToAdd = ChatMessage.builder()
                .userId(123L)
                .supportCase(supportCase)
                .timestamp(now)
                .content("message")
                .build();

        ChatMessage addedChatMessage = chatMessageToAdd.toBuilder().id(12345L).build();

        when(clock.instant()).thenReturn(now);
        when(mapper.toChatMessage(chatMessageDto)).thenReturn(chatMessageToAdd);
        when(messageRepository.save(any())).thenReturn(addedChatMessage);
        when(supportCasesRepository.findById(456L)).thenReturn(Optional.of(supportCaseWithMessages));

        // When
        ChatMessage result = chatService.addMessage(chatMessageDto);

        // Then
        assertThat(result).isEqualTo(addedChatMessage);
        assertThat(supportCaseWithMessages.getMessages()).containsExactly(
                ChatMessage.builder().id(1L).build(),
                ChatMessage.builder().id(2L).build(),
                ChatMessage.builder().id(3L).build(),
                addedChatMessage);
    }

    @Test
    void addMessage_supportCaseNotExists_messageNotAdded() {
        // Given
        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .userId(123L)
                .supportCaseId(456L)
                .content("message")
                .build();
        ChatMessage chatMessage = ChatMessage.builder()
                .userId(123L)
                .content("message")
                .build();

        when(messageRepository.save(any())).thenReturn(chatMessage);
        when(mapper.toChatMessage(any())).thenReturn(chatMessage);

        // When
        ChatMessage result = chatService.addMessage(chatMessageDto);

        // Then
        assertEquals(123, result.getUserId());
        assertEquals("message", result.getContent());
    }
}