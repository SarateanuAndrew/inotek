package org.example.exportproback.marketplace.repository;

import org.example.exportproback.marketplace.domain.ContactInquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContactInquiryRepository extends JpaRepository<ContactInquiry, UUID> {
    List<ContactInquiry> findByListingIdOrderByCreatedAtDesc(UUID listingId);
}
