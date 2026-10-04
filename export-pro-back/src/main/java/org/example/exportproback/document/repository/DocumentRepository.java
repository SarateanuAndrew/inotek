package org.example.exportproback.document.repository;

import org.example.exportproback.document.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    List<Document> findByCompanyId(UUID companyId);
    List<Document> findByComplianceCaseId(UUID complianceCaseId);
}
