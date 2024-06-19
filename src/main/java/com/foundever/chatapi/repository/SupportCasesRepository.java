package com.foundever.chatapi.repository;

import com.foundever.chatapi.model.entity.SupportCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportCasesRepository extends JpaRepository<SupportCase, Long> {}
