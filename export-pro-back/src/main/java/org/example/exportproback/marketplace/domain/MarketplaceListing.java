package org.example.exportproback.marketplace.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.exportproback.common.domain.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "marketplace_listings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketplaceListing extends BaseEntity {

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private UUID companyId;

    @Column(nullable = false)
    private UUID complianceCaseId;

    @Column(nullable = false)
    private String title;

    @Column(length = 3000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ListingStatus status;

    private String certificationSummary;

    private String targetMarkets;

    private Integer minimumOrderQuantity;

    private String productionCapacity;

    private LocalDateTime publishedAt;
}
