package org.example.exportproback.distributor.repository;

import org.example.exportproback.distributor.domain.Distributor;
import org.example.exportproback.distributor.domain.DistributorStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DistributorRepository extends JpaRepository<Distributor, UUID> {
    Optional<Distributor> findByUserId(UUID userId);
    List<Distributor> findByStatus(DistributorStatus status);
    boolean existsByCui(String cui);
}
