package org.example.exportproback.ai.dto;

import lombok.Data;

import java.util.List;

@Data
public class CertificationRecommendRequest {
    private String productType;
    private String productCategory;
    private String targetCountry = "RO";
    private boolean isOrganic;
    private boolean isAnimalProduct;
    private List<String> currentCertifications = List.of();
    private String companyDescription;
}
