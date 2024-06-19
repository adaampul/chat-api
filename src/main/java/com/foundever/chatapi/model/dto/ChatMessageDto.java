package com.foundever.chatapi.model.dto;

import lombok.Builder;

import java.time.Instant;

@Builder(toBuilder = true)
public record ChatMessageDto(
    Long id, Instant timestamp, String content, String username, Long userId, Long supportCaseId) {}
