from fastapi import APIRouter
from app.models.certification import CertificationRecommendRequest, CertificationRecommendResponse
from app.services.certification_advisor import recommend_certifications

router = APIRouter(prefix="/v1/certifications", tags=["certifications"])


@router.post("/recommend", response_model=CertificationRecommendResponse)
async def recommend(request: CertificationRecommendRequest):
    return await recommend_certifications(request)
