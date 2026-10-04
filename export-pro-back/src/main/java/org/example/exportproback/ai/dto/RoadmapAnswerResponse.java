package org.example.exportproback.ai.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RoadmapAnswerResponse {
    private String message;
    private String axis;
    private Boolean isComplete;
    private Map<String, Integer> scores;
    private List<AxisAdviceDto> advice;
}
