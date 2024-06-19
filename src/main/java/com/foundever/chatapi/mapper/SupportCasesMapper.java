package com.foundever.chatapi.mapper;

import com.foundever.chatapi.model.entity.SupportCase;
import com.foundever.chatapi.model.dto.SupportCaseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ChatMessagesMapper.class)
public interface SupportCasesMapper {

  SupportCase toSupportCase(SupportCaseDto supportCaseDto);

  SupportCaseDto toSupportCaseDto(SupportCase supportCase);
}
