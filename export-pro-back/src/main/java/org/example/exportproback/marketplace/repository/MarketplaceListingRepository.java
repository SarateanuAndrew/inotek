package org.example.exportproback.marketplace.repository;

import org.example.exportproback.marketplace.domain.ListingStatus;
import org.example.exportproback.marketplace.domain.MarketplaceListing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MarketplaceListingRepository extends JpaRepository<MarketplaceListing, UUID> {
    List<MarketplaceListing> findByStatus(ListingStatus status);
    List<MarketplaceListing> findByCompanyId(UUID companyId);
}
