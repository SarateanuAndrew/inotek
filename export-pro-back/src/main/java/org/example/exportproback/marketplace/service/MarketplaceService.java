package org.example.exportproback.marketplace.service;

import lombok.RequiredArgsConstructor;
import org.example.exportproback.common.exception.ResourceNotFoundException;
import org.example.exportproback.company.repository.CompanyRepository;
import org.example.exportproback.compliance.repository.ComplianceCaseRepository;
import org.example.exportproback.marketplace.domain.ContactInquiry;
import org.example.exportproback.marketplace.domain.ListingStatus;
import org.example.exportproback.marketplace.domain.MarketplaceListing;
import org.example.exportproback.marketplace.dto.*;
import org.example.exportproback.marketplace.repository.ContactInquiryRepository;
import org.example.exportproback.marketplace.repository.MarketplaceListingRepository;
import org.example.exportproback.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MarketplaceService {

    private final MarketplaceListingRepository listingRepository;
    private final ContactInquiryRepository inquiryRepository;
    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final ComplianceCaseRepository complianceCaseRepository;

    public MarketplaceListingDto create(CreateListingRequest request) {
        MarketplaceListing listing = MarketplaceListing.builder()
                .productId(request.getProductId())
                .companyId(request.getCompanyId())
                .complianceCaseId(request.getComplianceCaseId())
                .title(request.getTitle())
                .description(request.getDescription())
                .certificationSummary(request.getCertificationSummary())
                .targetMarkets(request.getTargetMarkets())
                .minimumOrderQuantity(request.getMinimumOrderQuantity())
                .productionCapacity(request.getProductionCapacity())
                .status(ListingStatus.DRAFT)
                .build();
        return toDto(listingRepository.save(listing));
    }

    public List<MarketplaceListingDto> findPublished() {
        return listingRepository.findByStatus(ListingStatus.PUBLISHED).stream().map(this::toDto).toList();
    }

    public MarketplaceListingDto findById(UUID id) {
        return toDto(listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MarketplaceListing", id)));
    }

    public List<MarketplaceListingDto> findByCompanyId(UUID companyId) {
        return listingRepository.findByCompanyId(companyId).stream().map(this::toDto).toList();
    }

    public MarketplaceListingDto publish(UUID id) {
        MarketplaceListing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MarketplaceListing", id));
        listing.setStatus(ListingStatus.PUBLISHED);
        listing.setPublishedAt(LocalDateTime.now());
        return toDto(listingRepository.save(listing));
    }

    public MarketplaceListingDto update(UUID id, CreateListingRequest request) {
        MarketplaceListing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MarketplaceListing", id));
        listing.setTitle(request.getTitle());
        listing.setDescription(request.getDescription());
        listing.setCertificationSummary(request.getCertificationSummary());
        listing.setTargetMarkets(request.getTargetMarkets());
        listing.setMinimumOrderQuantity(request.getMinimumOrderQuantity());
        listing.setProductionCapacity(request.getProductionCapacity());
        return toDto(listingRepository.save(listing));
    }

    public ContactInquiryDto sendInquiry(UUID listingId, SendInquiryRequest request) {
        if (!listingRepository.existsById(listingId)) {
            throw new ResourceNotFoundException("MarketplaceListing", listingId);
        }
        ContactInquiry inquiry = ContactInquiry.builder()
                .listingId(listingId)
                .buyerName(request.getBuyerName())
                .buyerEmail(request.getBuyerEmail())
                .buyerCompany(request.getBuyerCompany())
                .message(request.getMessage())
                .read(false)
                .build();
        return toInquiryDto(inquiryRepository.save(inquiry));
    }

    public List<ContactInquiryDto> getInquiries(UUID listingId) {
        return inquiryRepository.findByListingIdOrderByCreatedAtDesc(listingId)
                .stream().map(this::toInquiryDto).toList();
    }

    private MarketplaceListingDto toDto(MarketplaceListing m) {
        MarketplaceListingDto dto = new MarketplaceListingDto();
        dto.setId(m.getId());
        dto.setProductId(m.getProductId());
        dto.setCompanyId(m.getCompanyId());
        dto.setComplianceCaseId(m.getComplianceCaseId());
        dto.setTitle(m.getTitle());
        dto.setDescription(m.getDescription());
        dto.setStatus(m.getStatus());
        dto.setCertificationSummary(m.getCertificationSummary());
        dto.setTargetMarkets(m.getTargetMarkets());
        dto.setMinimumOrderQuantity(m.getMinimumOrderQuantity());
        dto.setProductionCapacity(m.getProductionCapacity());
        dto.setPublishedAt(m.getPublishedAt());
        dto.setCreatedAt(m.getCreatedAt());

        companyRepository.findById(m.getCompanyId()).ifPresent(c -> {
            dto.setCompanyName(c.getName());
            dto.setCompanyCountry(c.getCountry());
            dto.setCompanyWebsite(c.getWebsite());
            dto.setCompanyPhone(c.getPhoneNumber());
        });

        productRepository.findById(m.getProductId()).ifPresent(p -> {
            dto.setProductName(p.getName());
            dto.setProductCategory(p.getCategory() != null ? p.getCategory().name() : null);
        });

        complianceCaseRepository.findById(m.getComplianceCaseId()).ifPresent(cc ->
                dto.setReadinessScore(cc.getReadinessScore()));

        return dto;
    }

    private ContactInquiryDto toInquiryDto(ContactInquiry i) {
        ContactInquiryDto dto = new ContactInquiryDto();
        dto.setId(i.getId());
        dto.setListingId(i.getListingId());
        dto.setBuyerName(i.getBuyerName());
        dto.setBuyerEmail(i.getBuyerEmail());
        dto.setBuyerCompany(i.getBuyerCompany());
        dto.setMessage(i.getMessage());
        dto.setRead(i.isRead());
        dto.setCreatedAt(i.getCreatedAt());
        return dto;
    }
}
