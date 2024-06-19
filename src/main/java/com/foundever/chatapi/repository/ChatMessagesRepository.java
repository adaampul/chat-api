package com.foundever.chatapi.repository;

import com.foundever.chatapi.model.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessagesRepository extends JpaRepository<ChatMessage, Long> {}
