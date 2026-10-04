package org.example.exportproback.compliance.service;

import lombok.RequiredArgsConstructor;
import org.example.exportproback.common.exception.ResourceNotFoundException;
import org.example.exportproback.compliance.domain.*;
import org.example.exportproback.compliance.dto.*;
import org.example.exportproback.compliance.repository.ComplianceCaseRepository;
import org.example.exportproback.compliance.repository.RequirementRepository;
import org.example.exportproback.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ComplianceCaseService {

    private final ComplianceCaseRepository complianceCaseRepository;
    private final RequirementRepository requirementRepository;
    private final ProductRepository productRepository;

    @Transactional
    public ComplianceCaseDto create(CreateComplianceCaseRequest request) {
        ComplianceCase complianceCase = ComplianceCase.builder()
                .caseNumber(generateCaseNumber())
                .productId(request.getProductId())
                .companyId(request.getCompanyId())
                .originCountry(request.getOriginCountry())
                .targetCountry(request.getTargetCountry())
                .status(ComplianceCaseStatus.DRAFT)
                .readinessScore(0)
                .build();
        return toDto(complianceCaseRepository.save(complianceCase));
    }

    public List<ComplianceCaseDto> findByCompanyId(UUID companyId) {
        return complianceCaseRepository.findByCompanyId(companyId).stream().map(this::toDto).toList();
    }

    public ComplianceCaseDto findById(UUID id) {
        return toDto(complianceCaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ComplianceCase", id)));
    }

    public List<RequirementDto> getRequirements(UUID caseId) {
        return requirementRepository.findByComplianceCaseId(caseId).stream().map(this::toRequirementDto).toList();
    }

    @Transactional
    public ComplianceCaseDto updateStatus(UUID id, ComplianceCaseStatus newStatus) {
        ComplianceCase c = complianceCaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ComplianceCase", id));
        c.setStatus(newStatus);
        recalculateReadiness(c);
        return toDto(complianceCaseRepository.save(c));
    }

    private void recalculateReadiness(ComplianceCase c) {
        List<Requirement> requirements = requirementRepository.findByComplianceCaseId(c.getId());
        if (requirements.isEmpty()) return;
        long verified = requirements.stream().filter(r -> r.getStatus() == RequirementStatus.VERIFIED).count();
        c.setReadinessScore((int) ((verified * 100) / requirements.size()));
    }

    private String generateCaseNumber() {
        return "CASE-" + System.currentTimeMillis() + "-" + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    private ComplianceCaseDto toDto(ComplianceCase c) {
        ComplianceCaseDto dto = new ComplianceCaseDto();
        dto.setId(c.getId());
        dto.setCaseNumber(c.getCaseNumber());
        dto.setProductId(c.getProductId());
        productRepository.findById(c.getProductId()).ifPresent(p -> dto.setProductName(p.getName()));
        dto.setCompanyId(c.getCompanyId());
        dto.setOriginCountry(c.getOriginCountry());
        dto.setTargetCountry(c.getTargetCountry());
        dto.setStatus(c.getStatus());
        dto.setReadinessScore(c.getReadinessScore());
        dto.setAiAnalysisSummary(c.getAiAnalysisSummary());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setUpdatedAt(c.getUpdatedAt());
        return dto;
    }

    private RequirementDto toRequirementDto(Requirement r) {
        RequirementDto dto = new RequirementDto();
        dto.setId(r.getId());
        dto.setComplianceCaseId(r.getComplianceCaseId());
        dto.setCode(r.getCode());
        dto.setType(r.getType());
        dto.setTitle(r.getTitle());
        dto.setDescription(r.getDescription());
        dto.setStatus(r.getStatus());
        dto.setLegalBasis(r.getLegalBasis());
        dto.setSourceUrl(r.getSourceUrl());
        dto.setAiGenerated(r.isAiGenerated());
        dto.setAiConfidence(r.getAiConfidence());
        dto.setEvidenceRequired(r.getEvidenceRequired());
        return dto;
    }
}
