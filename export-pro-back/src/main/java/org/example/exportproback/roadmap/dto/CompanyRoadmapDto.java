package org.example.exportproback.roadmap.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class CompanyRoadmapDto {
    private UUID id;
    private UUID companyId;
    private Map<String, Integer> scores;
    private List<AxisAdviceDto> advice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
