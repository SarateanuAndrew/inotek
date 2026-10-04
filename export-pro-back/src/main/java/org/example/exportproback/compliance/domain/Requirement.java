package org.example.exportproback.compliance.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.exportproback.common.domain.BaseEntity;

import java.util.UUID;

@Entity
@Table(name = "requirements")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Requirement extends BaseEntity {

    @Column(nullable = false)
    private UUID complianceCaseId;

    @Column(nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequirementType type;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequirementStatus status;

    private String legalBasis;

    private String sourceUrl;

    private boolean aiGenerated;

    private double aiConfidence;

    private String evidenceRequired;
}
