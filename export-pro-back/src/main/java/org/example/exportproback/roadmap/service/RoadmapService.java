package org.example.exportproback.roadmap.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.exportproback.roadmap.domain.CompanyRoadmap;
import org.example.exportproback.roadmap.dto.AxisAdviceDto;
import org.example.exportproback.roadmap.dto.CompanyRoadmapDto;
import org.example.exportproback.roadmap.dto.SaveRoadmapRequest;
import org.example.exportproback.roadmap.repository.CompanyRoadmapRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoadmapService {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final CompanyRoadmapRepository repository;

    public CompanyRoadmapDto save(UUID companyId, SaveRoadmapRequest request) {
        try {
            CompanyRoadmap roadmap = CompanyRoadmap.builder()
                    .companyId(companyId)
                    .scoresJson(MAPPER.writeValueAsString(request.getScores()))
                    .adviceJson(MAPPER.writeValueAsString(request.getAdvice()))
                    .build();
            return toDto(repository.save(roadmap));
        } catch (Exception e) {
            throw new RuntimeException("Failed to save roadmap", e);
        }
    }

    public Optional<CompanyRoadmapDto> findLatest(UUID companyId) {
        return repository.findTopByCompanyIdOrderByCreatedAtDesc(companyId).map(this::toDto);
    }

    private CompanyRoadmapDto toDto(CompanyRoadmap r) {
        CompanyRoadmapDto dto = new CompanyRoadmapDto();
        dto.setId(r.getId());
        dto.setCompanyId(r.getCompanyId());
        dto.setCreatedAt(r.getCreatedAt());
        dto.setUpdatedAt(r.getUpdatedAt());
        try {
            dto.setScores(MAPPER.readValue(r.getScoresJson(), new TypeReference<Map<String, Integer>>() {}));
            dto.setAdvice(MAPPER.readValue(r.getAdviceJson(), new TypeReference<List<AxisAdviceDto>>() {}));
        } catch (Exception e) {
            dto.setScores(Map.of());
            dto.setAdvice(List.of());
        }
        return dto;
    }
}
