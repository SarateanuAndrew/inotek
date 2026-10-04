package org.example.exportproback.compliance.repository;

import org.example.exportproback.compliance.domain.Requirement;
import org.example.exportproback.compliance.domain.RequirementStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RequirementRepository extends JpaRepository<Requirement, UUID> {
    List<Requirement> findByComplianceCaseId(UUID complianceCaseId);
    List<Requirement> findByComplianceCaseIdAndStatus(UUID complianceCaseId, RequirementStatus status);
}
