package org.example.exportproback.ai.dto;

import lombok.Data;
import java.util.List;

@Data
public class RequirementItem {
    private String code;
    private String type;
    private String title;
    private String status;
    private String legalBasis;
    private String sourceUrl;
    private String reason;
    private List<String> evidenceRequired;
    private double confidence;
}
