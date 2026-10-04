package org.example.exportproback.document.controller;

import lombok.RequiredArgsConstructor;
import org.example.exportproback.common.dto.ApiResponse;
import org.example.exportproback.document.domain.Document;
import org.example.exportproback.document.domain.DocumentStatus;
import org.example.exportproback.document.dto.DocumentDto;
import org.example.exportproback.document.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<DocumentDto>> upload(
            @RequestParam UUID companyId,
            @RequestParam(required = false) UUID complianceCaseId,
            @RequestParam(required = false) String documentType,
            @RequestParam MultipartFile file) {
        String storageKey = "uploads/" + companyId + "/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Document doc = Document.builder()
                .companyId(companyId)
                .complianceCaseId(complianceCaseId)
                .documentType(documentType)
                .originalFilename(file.getOriginalFilename())
                .storageKey(storageKey)
                .mimeType(file.getContentType())
                .status(DocumentStatus.PENDING)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(documentService.save(doc)));
    }

    @GetMapping("/compliance-case/{complianceCaseId}")
    public ResponseEntity<ApiResponse<List<DocumentDto>>> getByCaseId(@PathVariable UUID complianceCaseId) {
        return ResponseEntity.ok(ApiResponse.ok(documentService.findByComplianceCaseId(complianceCaseId)));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<List<DocumentDto>>> getByCompanyId(@PathVariable UUID companyId) {
        return ResponseEntity.ok(ApiResponse.ok(documentService.findByCompanyId(companyId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(documentService.findById(id)));
    }
}
