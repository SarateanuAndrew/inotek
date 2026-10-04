package org.example.exportproback.compliance.repository;

import org.example.exportproback.compliance.domain.ComplianceCase;
import org.example.exportproback.compliance.domain.ComplianceCaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ComplianceCaseRepository extends JpaRepository<ComplianceCase, UUID> {
    List<ComplianceCase> findByCompanyId(UUID companyId);
    List<ComplianceCase> findByProductId(UUID productId);
    List<ComplianceCase> findByStatus(ComplianceCaseStatus status);
    boolean existsByCaseNumber(String caseNumber);
}
