package org.example.exportproback.roadmap.repository;

import org.example.exportproback.roadmap.domain.CompanyRoadmap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRoadmapRepository extends JpaRepository<CompanyRoadmap, UUID> {
    Optional<CompanyRoadmap> findTopByCompanyIdOrderByCreatedAtDesc(UUID companyId);
}
