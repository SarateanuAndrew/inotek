package org.example.exportproback.ai.client;

import lombok.RequiredArgsConstructor;
import org.example.exportproback.ai.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class AiServiceClient {

    @Value("${app.ai.base-url}")
    private String aiBaseUrl;

    public ProductAnalysisResponse analyzeProduct(ProductAnalysisRequest request) {
        return restClient().post()
                .uri("/v1/compliance/analyze")
                .body(request)
                .retrieve()
                .body(ProductAnalysisResponse.class);
    }

    public CertificationRecommendResponse recommendCertifications(CertificationRecommendRequest request) {
        return restClient().post()
                .uri("/v1/certifications/recommend")
                .body(request)
                .retrieve()
                .body(CertificationRecommendResponse.class);
    }

    private RestClient restClient() {
        return RestClient.create(aiBaseUrl);
    }
}
