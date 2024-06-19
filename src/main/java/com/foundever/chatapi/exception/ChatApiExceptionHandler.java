package com.foundever.chatapi.exception;

import com.foundever.chatapi.model.dto.ErrorMessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChatApiExceptionHandler {

    @ExceptionHandler(ChatApiException.class)
    ResponseEntity<ErrorMessageDto> onChatApiExceptionResponse(ChatApiException chatApiException) {
        return ResponseEntity.status(chatApiException.getStatusCode()).body(new ErrorMessageDto(chatApiException.getReason(), chatApiException.getStatusCode()));
    }
    
}
