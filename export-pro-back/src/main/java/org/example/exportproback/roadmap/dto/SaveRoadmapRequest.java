package org.example.exportproback.roadmap.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SaveRoadmapRequest {
    private Map<String, Integer> scores;
    private List<AxisAdviceDto> advice;
}
