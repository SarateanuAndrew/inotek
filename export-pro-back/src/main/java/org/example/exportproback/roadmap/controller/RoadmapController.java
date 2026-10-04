package org.example.exportproback.roadmap.controller;

import lombok.RequiredArgsConstructor;
import org.example.exportproback.common.dto.ApiResponse;
import org.example.exportproback.roadmap.dto.CompanyRoadmapDto;
import org.example.exportproback.roadmap.dto.SaveRoadmapRequest;
import org.example.exportproback.roadmap.service.RoadmapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roadmap")
@RequiredArgsConstructor
public class RoadmapController {

    private final RoadmapService roadmapService;

    @PostMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<CompanyRoadmapDto>> save(
            @PathVariable UUID companyId,
            @RequestBody SaveRoadmapRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(roadmapService.save(companyId, request)));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<CompanyRoadmapDto>> getLatest(@PathVariable UUID companyId) {
        return roadmapService.findLatest(companyId)
                .map(dto -> ResponseEntity.ok(ApiResponse.ok(dto)))
                .orElse(ResponseEntity.notFound().build());
    }
}
