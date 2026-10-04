package org.example.exportproback.document.dto;

import lombok.Data;
import org.example.exportproback.document.domain.DocumentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DocumentDto {
    private UUID id;
    private UUID companyId;
    private UUID complianceCaseId;
    private String documentType;
    private String originalFilename;
    private String storageKey;
    private String mimeType;
    private DocumentStatus status;
    private String issuer;
    private String documentNumber;
    private LocalDate issuedDate;
    private LocalDate expiryDate;
    private Double aiConfidence;
    private LocalDateTime createdAt;
}
