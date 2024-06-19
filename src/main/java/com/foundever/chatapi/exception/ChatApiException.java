package com.foundever.chatapi.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class ChatApiException extends ResponseStatusException {
    public ChatApiException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
