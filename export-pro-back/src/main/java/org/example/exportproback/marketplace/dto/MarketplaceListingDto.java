package org.example.exportproback.marketplace.dto;

import lombok.Data;
import org.example.exportproback.marketplace.domain.ListingStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MarketplaceListingDto {
    private UUID id;
    private UUID productId;
    private UUID companyId;
    private UUID complianceCaseId;
    private String title;
    private String description;
    private ListingStatus status;
    private String certificationSummary;
    private String targetMarkets;
    private Integer minimumOrderQuantity;
    private String productionCapacity;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;

    // Enriched fields
    private String companyName;
    private String companyCountry;
    private String companyWebsite;
    private String companyPhone;
    private String productName;
    private String productCategory;
    private Integer readinessScore;
}
