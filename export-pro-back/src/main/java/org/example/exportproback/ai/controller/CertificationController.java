package org.example.exportproback.ai.controller;

import lombok.RequiredArgsConstructor;
import org.example.exportproback.ai.client.AiServiceClient;
import org.example.exportproback.ai.dto.CertificationRecommendRequest;
import org.example.exportproback.ai.dto.CertificationRecommendResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/certifications")
@RequiredArgsConstructor
public class CertificationController {

    private final AiServiceClient aiServiceClient;

    @PostMapping("/recommend")
    public ResponseEntity<CertificationRecommendResponse> recommend(
            @RequestBody CertificationRecommendRequest request) {
        return ResponseEntity.ok(aiServiceClient.recommendCertifications(request));
    }
}
