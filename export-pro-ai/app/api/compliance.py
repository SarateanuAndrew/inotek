from fastapi import APIRouter, HTTPException
from app.models.compliance import AnalysisRequest, AnalysisResponse
from app.services.classifier import classify_product
from app.services.rules_engine import apply_rules
from app.services.explainer import enrich_requirements_with_explanations, generate_analysis_summary

router = APIRouter(prefix="/v1/compliance", tags=["compliance"])


@router.post("/analyze", response_model=AnalysisResponse)
async def analyze_product(request: AnalysisRequest) -> AnalysisResponse:
    try:
        classification = await classify_product(request.product)

        requirements = apply_rules(
            company_country=request.company_country,
            target_country=request.target_country,
            classification=classification,
            product_organic=request.product.organic
        )

        requirements = await enrich_requirements_with_explanations(
            requirements=requirements,
            product=request.product,
            company_country=request.company_country,
            target_country=request.target_country
        )

        required_count = sum(1 for r in requirements if r.status.value == "REQUIRED")
        overall_confidence = sum(r.confidence for r in requirements) / len(requirements) if requirements else 0.5
        readiness_pct = 0

        summary = await generate_analysis_summary(
            product=request.product,
            requirements=requirements,
            readiness_pct=readiness_pct
        )

        missing_info = []
        if not classification.suggested_hs_code:
            missing_info.append("HS/CN product classification code — provide for precise customs requirements")

        return AnalysisResponse(
            compliance_case_id=request.compliance_case_id,
            product_classification=classification,
            requirements=requirements,
            missing_information=missing_info,
            warnings=[],
            overall_confidence=round(overall_confidence, 2),
            analysis_summary=summary
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
