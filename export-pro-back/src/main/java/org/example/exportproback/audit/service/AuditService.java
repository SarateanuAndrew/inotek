package org.example.exportproback.audit.service;

import lombok.RequiredArgsConstructor;
import org.example.exportproback.audit.domain.AuditEvent;
import org.example.exportproback.audit.domain.AuditEventType;
import org.example.exportproback.audit.repository.AuditEventRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditEventRepository auditEventRepository;

    @Async
    public void log(UUID actorId, String entityType, UUID entityId, AuditEventType eventType, String metadata) {
        AuditEvent event = AuditEvent.builder()
                .actorId(actorId)
                .entityType(entityType)
                .entityId(entityId)
                .eventType(eventType)
                .timestamp(LocalDateTime.now())
                .metadata(metadata)
                .build();
        auditEventRepository.save(event);
    }
}
