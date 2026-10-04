package org.example.exportproback.ai.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProductAnalysisResponse {
    private ProductClassification productClassification;
    private List<RequirementItem> requirements;
    private List<String> missingInformation;
    private List<String> warnings;
    private double overallConfidence;

    @Data
    public static class ProductClassification {
        private String category;
        private String subCategory;
        private String suggestedHsCode;
        private double confidence;
    }
}
