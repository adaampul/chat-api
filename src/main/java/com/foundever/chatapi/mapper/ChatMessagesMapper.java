package com.foundever.chatapi.mapper;

import com.foundever.chatapi.model.entity.ChatMessage;
import com.foundever.chatapi.model.entity.SupportCase;
import com.foundever.chatapi.model.dto.ChatMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ChatMessagesMapper {

  @Mapping(target = "supportCase", source = "supportCaseId")
  ChatMessage toChatMessage(ChatMessageDto chatMessageDto);

  @Mapping(target = "supportCaseId", source = "supportCase.id")
  ChatMessageDto toChatMessageDto(ChatMessage chatMessage);

  default SupportCase fromId(Long id) {
    return Optional.ofNullable(id)
            .map(id1 -> SupportCase.builder().id(id1).build())
            .orElse(null);
  }
}
