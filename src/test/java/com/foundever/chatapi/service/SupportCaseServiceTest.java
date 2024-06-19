package com.foundever.chatapi.service;

import com.foundever.chatapi.exception.ChatApiException;
import com.foundever.chatapi.model.entity.SupportCase;
import com.foundever.chatapi.model.dto.SupportCaseDto;
import com.foundever.chatapi.repository.SupportCasesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SupportCaseServiceTest {

  @Mock
  private SupportCasesRepository supportCasesRepository;

  @InjectMocks
  private SupportCaseService supportCaseService;

  @Test
  void testPatchSupportCase_Success() {
    Long supportCaseId = 1L;
    SupportCaseDto supportCaseDto = new SupportCaseDto(supportCaseId, "UPDATED-REF", null);
    SupportCase existingSupportCase = new SupportCase(supportCaseId, "OLD-REF", null);

    when(supportCasesRepository.findById(any())).thenReturn(Optional.of(existingSupportCase));

    SupportCase patchedSupportCase = supportCaseService.patchSupportCase(supportCaseId, supportCaseDto);

    assertNotNull(patchedSupportCase);
    assertEquals(supportCaseDto.clientReference(), patchedSupportCase.getClientReference());

    verify(supportCasesRepository, times(1)).findById(1L);
  }

  @Test
  void testPatchSupportCase_NotFound_ExceptionThrown() {
    Long nonExistingId = 999L;
    SupportCaseDto supportCaseDto = new SupportCaseDto(nonExistingId, "UPDATED-REF", null);

    when(supportCasesRepository.findById(any())).thenReturn(Optional.empty());

    ChatApiException exception = assertThrows(ChatApiException.class, () -> supportCaseService.patchSupportCase(nonExistingId, supportCaseDto));
    assertEquals("Support case with given id: '999' does not exist", exception.getReason());

    verify(supportCasesRepository, times(1)).findById(any());
    verify(supportCasesRepository, never()).save(any());
  }
}
