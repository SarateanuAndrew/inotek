package org.example.exportproback.compliance.dto;

import lombok.Data;
import org.example.exportproback.compliance.domain.ComplianceCaseStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ComplianceCaseDto {
    private UUID id;
    private String caseNumber;
    private UUID productId;
    private String productName;
    private UUID companyId;
    private String originCountry;
    private String targetCountry;
    private ComplianceCaseStatus status;
    private int readinessScore;
    private String aiAnalysisSummary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
