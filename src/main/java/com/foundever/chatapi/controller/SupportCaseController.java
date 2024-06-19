package com.foundever.chatapi.controller;

import com.foundever.chatapi.mapper.SupportCasesMapper;
import com.foundever.chatapi.model.dto.SupportCaseDto;
import com.foundever.chatapi.service.SupportCaseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/support-cases")
public class SupportCaseController {

  private final SupportCaseService supportCaseService;
  private final SupportCasesMapper supportCasesMapper;

  @GetMapping
  List<SupportCaseDto> getSupportCases(Pageable pageable) {
    log.atTrace().log("Get support cases request");
    return supportCaseService.getAll(pageable).stream().map(supportCasesMapper::toSupportCaseDto).toList();
  }

  @PostMapping
  SupportCaseDto createSupportCase(@RequestBody SupportCaseDto supportCaseDto) {
    log.atTrace().log("Create support case request: {}", supportCaseDto);
    var supportCase = supportCaseService.createSupportCase(supportCasesMapper.toSupportCase(supportCaseDto));
    log.atTrace().log("Saved new support case: {}", supportCase);
    return supportCasesMapper.toSupportCaseDto(supportCase);
  }

  @PatchMapping("/{id}")
  SupportCaseDto updateSupportCase(@PathVariable Long id, @RequestBody SupportCaseDto supportCaseDto) {
    log.atTrace().log("Update support case with id: {}, new data {}", id, supportCaseDto);
    var supportCase = supportCaseService.patchSupportCase(id, supportCaseDto);
    log.atTrace().log("Support case after update: {}", supportCase);
    return supportCasesMapper.toSupportCaseDto(supportCase);
  }

}
