from fastapi import APIRouter, HTTPException
from app.models.document import DocumentAnalysisRequest, DocumentAnalysisResponse
from app.services.document_analyzer import analyze_document

router = APIRouter(prefix="/v1/documents", tags=["documents"])


@router.post("/analyze", response_model=DocumentAnalysisResponse)
async def analyze_document_endpoint(request: DocumentAnalysisRequest) -> DocumentAnalysisResponse:
    try:
        return await analyze_document(request)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
