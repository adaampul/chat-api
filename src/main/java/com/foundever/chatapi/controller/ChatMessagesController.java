package com.foundever.chatapi.controller;

import com.foundever.chatapi.mapper.ChatMessagesMapper;
import com.foundever.chatapi.model.dto.ChatMessageDto;
import com.foundever.chatapi.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class ChatMessagesController {

  private final ChatService chatService;
  private final ChatMessagesMapper chatMessagesMapper;

  @PostMapping
  ChatMessageDto addMessage(@RequestBody ChatMessageDto chatMessageDto) {
    log.atTrace().log("Received new add message request: '{}'", chatMessageDto);
    var savedMessage = chatService.addMessage(chatMessageDto);
    log.atTrace().log("Saved new message: {}", savedMessage);
    return chatMessagesMapper.toChatMessageDto(savedMessage);
  }
}
