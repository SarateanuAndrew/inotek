package org.example.exportproback.ai.dto;

import lombok.Data;

import java.util.List;

@Data
public class CertificationRecommendResponse {
    private String productType;
    private int totalRequired;
    private int totalRecommended;
    private List<CertificationItemDto> certifications;
    private String summary;
}
