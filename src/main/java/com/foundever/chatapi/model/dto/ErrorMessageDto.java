package com.foundever.chatapi.model.dto;

import org.springframework.http.HttpStatusCode;

public record ErrorMessageDto(String message, HttpStatusCode httpStatusCode) {
}
