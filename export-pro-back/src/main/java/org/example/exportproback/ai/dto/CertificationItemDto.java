package org.example.exportproback.ai.dto;

import lombok.Data;

import java.util.List;

@Data
public class CertificationItemDto {
    private String code;
    private String name;
    private String priority;
    private String description;
    private String legalBasis;
    private String estimatedCost;
    private String estimatedTime;
    private List<String> steps;
}
