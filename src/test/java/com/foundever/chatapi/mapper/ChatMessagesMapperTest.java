package com.foundever.chatapi.mapper;

import com.foundever.chatapi.model.entity.ChatMessage;
import com.foundever.chatapi.model.entity.SupportCase;
import com.foundever.chatapi.model.dto.ChatMessageDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class ChatMessagesMapperTest {

  private final ChatMessagesMapper mapper = Mappers.getMapper(ChatMessagesMapper.class);

  @Test
  void testToChatMessageDto() {
    SupportCase supportCase = new SupportCase(1L, "REF-123", null);
    ChatMessage chatMessage = new ChatMessage(1L, Instant.now(), "Hello", "User1", 123L, supportCase);

    ChatMessageDto chatMessageDto = mapper.toChatMessageDto(chatMessage);

    assertNotNull(chatMessageDto);
    assertEquals(chatMessage.getId(), chatMessageDto.id());
    assertEquals(chatMessage.getTimestamp(), chatMessageDto.timestamp());
    assertEquals(chatMessage.getContent(), chatMessageDto.content());
    assertEquals(chatMessage.getUsername(), chatMessageDto.username());
    assertEquals(chatMessage.getUserId(), chatMessageDto.userId());
    assertEquals(chatMessage.getSupportCase().getId(), chatMessageDto.supportCaseId());
  }

  @Test
  void testToChatMessage() {
    ChatMessageDto chatMessageDto = new ChatMessageDto(1L, Instant.now(), "Hello", "User1", 123L, 1L);

    ChatMessage chatMessage = mapper.toChatMessage(chatMessageDto);

    assertNotNull(chatMessage);
    assertEquals(chatMessageDto.id(), chatMessage.getId());
    assertEquals(chatMessageDto.timestamp(), chatMessage.getTimestamp());
    assertEquals(chatMessageDto.content(), chatMessage.getContent());
    assertEquals(chatMessageDto.username(), chatMessage.getUsername());
    assertEquals(chatMessageDto.userId(), chatMessage.getUserId());
    assertNotNull(chatMessage.getSupportCase());
    assertEquals(chatMessageDto.supportCaseId(), chatMessage.getSupportCase().getId());
  }

  @Test
  void testFromId() {
    Long id = 1L;

    SupportCase supportCase = mapper.fromId(id);

    assertNotNull(supportCase);
    assertEquals(id, supportCase.getId());
    assertNull(supportCase.getClientReference());
    assertNull(supportCase.getMessages());
  }
}
