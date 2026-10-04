package org.example.exportproback.audit.repository;

import org.example.exportproback.audit.domain.AuditEvent;
import org.example.exportproback.audit.domain.AuditEventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditEventRepository extends JpaRepository<AuditEvent, UUID> {
    List<AuditEvent> findByEntityTypeAndEntityIdOrderByTimestampDesc(String entityType, UUID entityId);
    List<AuditEvent> findByActorIdOrderByTimestampDesc(UUID actorId);
    List<AuditEvent> findByEventTypeOrderByTimestampDesc(AuditEventType eventType);
}
