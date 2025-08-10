package com.learnexa.learnexaapi.repository;

import com.learnexa.learnexaapi.entity.PdfReference;
import com.learnexa.learnexaapi.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PdfReferenceRepository extends JpaRepository<PdfReference, Long> {
}
