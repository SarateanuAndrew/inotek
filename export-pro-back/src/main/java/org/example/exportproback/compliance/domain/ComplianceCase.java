package org.example.exportproback.compliance.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.exportproback.common.domain.BaseEntity;

import java.util.UUID;

@Entity
@Table(name = "compliance_cases")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceCase extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String caseNumber;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private UUID companyId;

    @Column(nullable = false, length = 3)
    private String originCountry;

    @Column(nullable = false, length = 3)
    private String targetCountry;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplianceCaseStatus status;

    @Column(nullable = false)
    private int readinessScore;

    private String aiAnalysisSummary;
}
