package org.example.exportproback.document.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.exportproback.common.domain.BaseEntity;

import java.util.UUID;

@Entity
@Table(name = "documents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document extends BaseEntity {

    @Column(nullable = false)
    private UUID companyId;

    private UUID complianceCaseId;

    private String documentType;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false)
    private String storageKey;

    private String mimeType;

    private String sha256;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus status;

    private String extractedText;

    private String issuer;

    private String documentNumber;

    private java.time.LocalDate issuedDate;

    private java.time.LocalDate expiryDate;

    private Double aiConfidence;
}
