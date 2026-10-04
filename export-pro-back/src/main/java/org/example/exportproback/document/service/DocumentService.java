package org.example.exportproback.document.service;

import lombok.RequiredArgsConstructor;
import org.example.exportproback.common.exception.ResourceNotFoundException;
import org.example.exportproback.document.domain.Document;
import org.example.exportproback.document.domain.DocumentStatus;
import org.example.exportproback.document.dto.DocumentDto;
import org.example.exportproback.document.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentDto save(Document document) {
        return toDto(documentRepository.save(document));
    }

    public List<DocumentDto> findByCompanyId(UUID companyId) {
        return documentRepository.findByCompanyId(companyId).stream().map(this::toDto).toList();
    }

    public List<DocumentDto> findByComplianceCaseId(UUID complianceCaseId) {
        return documentRepository.findByComplianceCaseId(complianceCaseId).stream().map(this::toDto).toList();
    }

    public DocumentDto findById(UUID id) {
        return toDto(documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document", id)));
    }

    public DocumentDto updateStatus(UUID id, DocumentStatus status) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document", id));
        doc.setStatus(status);
        return toDto(documentRepository.save(doc));
    }

    private DocumentDto toDto(Document d) {
        DocumentDto dto = new DocumentDto();
        dto.setId(d.getId());
        dto.setCompanyId(d.getCompanyId());
        dto.setComplianceCaseId(d.getComplianceCaseId());
        dto.setDocumentType(d.getDocumentType());
        dto.setOriginalFilename(d.getOriginalFilename());
        dto.setStorageKey(d.getStorageKey());
        dto.setMimeType(d.getMimeType());
        dto.setStatus(d.getStatus());
        dto.setIssuer(d.getIssuer());
        dto.setDocumentNumber(d.getDocumentNumber());
        dto.setIssuedDate(d.getIssuedDate());
        dto.setExpiryDate(d.getExpiryDate());
        dto.setAiConfidence(d.getAiConfidence());
        dto.setCreatedAt(d.getCreatedAt());
        return dto;
    }
}
