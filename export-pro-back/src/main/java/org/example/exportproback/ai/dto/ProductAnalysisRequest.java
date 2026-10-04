package org.example.exportproback.ai.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProductAnalysisRequest {
    private UUID complianceCaseId;
    private String companyCountry;
    private String targetCountry;
    private String productName;
    private String productCategory;
    private String ingredients;
    private String packagingType;
    private Integer weightGrams;
    private boolean organic;
}
