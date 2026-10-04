package org.example.exportproback.marketplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateListingRequest {
    @NotNull private UUID productId;
    @NotNull private UUID companyId;
    @NotNull private UUID complianceCaseId;
    @NotBlank private String title;
    private String description;
    private String certificationSummary;
    private String targetMarkets;
    private Integer minimumOrderQuantity;
    private String productionCapacity;
}
