package com.foundever.chatapi.model.dto;

import java.util.List;

public record SupportCaseDto(Long id, String clientReference, List<ChatMessageDto> messages) {}
