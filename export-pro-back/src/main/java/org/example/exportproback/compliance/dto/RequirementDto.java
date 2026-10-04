package org.example.exportproback.compliance.dto;

import lombok.Data;
import org.example.exportproback.compliance.domain.RequirementStatus;
import org.example.exportproback.compliance.domain.RequirementType;

import java.util.UUID;

@Data
public class RequirementDto {
    private UUID id;
    private UUID complianceCaseId;
    private String code;
    private RequirementType type;
    private String title;
    private String description;
    private RequirementStatus status;
    private String legalBasis;
    private String sourceUrl;
    private boolean aiGenerated;
    private double aiConfidence;
    private String evidenceRequired;
}
