import base64
import json
from datetime import date
from openai import AsyncOpenAI
from app.core.config import get_settings
from app.models.document import DocumentAnalysisRequest, DocumentAnalysisResponse, DocumentType


def extract_text_from_pdf(content_bytes: bytes) -> str:
    try:
        import fitz
        doc = fitz.open(stream=content_bytes, filetype="pdf")
        text = "\n".join(page.get_text() for page in doc)
        doc.close()
        return text[:4000]
    except Exception:
        return ""


def _parse_date(val) -> date | None:
    if val:
        try:
            return date.fromisoformat(val)
        except Exception:
            return None
    return None


async def analyze_document(request: DocumentAnalysisRequest) -> DocumentAnalysisResponse:
    settings = get_settings()
    content_bytes = base64.b64decode(request.file_content_base64)

    extracted_text = extract_text_from_pdf(content_bytes) if request.mime_type == "application/pdf" else ""

    if not extracted_text or not settings.openai_api_key:
        return DocumentAnalysisResponse(
            document_id=request.document_id,
            document_type=DocumentType.UNKNOWN,
            extracted_text=extracted_text,
            confidence=0.3,
            warnings=["Could not extract or analyze document content"]
        )

    client = AsyncOpenAI(api_key=settings.openai_api_key)
    prompt = f"""You are a document analysis expert for food industry certifications.

Analyze this document text and extract key information:

{extracted_text[:3000]}

Respond with a JSON object:
{{
  "document_type": one of: ISO_CERTIFICATE, HEALTH_CERTIFICATE, LAB_TEST, ORGANIC_CERTIFICATE, COMPANY_REGISTRATION, HACCP_CERTIFICATE, EXPORT_LICENSE, PHYTOSANITARY, UNKNOWN,
  "issuer": "issuing organization name or null",
  "company_name": "company this certificate was issued to or null",
  "certificate_number": "certificate/document number or null",
  "issued_date": "YYYY-MM-DD or null",
  "expiry_date": "YYYY-MM-DD or null",
  "scope": "what this certificate covers or null",
  "confidence": 0.0-1.0
}}"""

    try:
        response = await client.chat.completions.create(
            model=settings.ai_model,
            temperature=0.1,
            response_format={"type": "json_object"},
            messages=[{"role": "user", "content": prompt}]
        )
        data = json.loads(response.choices[0].message.content)
        return DocumentAnalysisResponse(
            document_id=request.document_id,
            document_type=DocumentType(data.get("document_type", "UNKNOWN")),
            issuer=data.get("issuer"),
            company_name=data.get("company_name"),
            certificate_number=data.get("certificate_number"),
            issued_date=_parse_date(data.get("issued_date")),
            expiry_date=_parse_date(data.get("expiry_date")),
            scope=data.get("scope"),
            extracted_text=extracted_text,
            confidence=float(data.get("confidence", 0.5))
        )
    except Exception as e:
        return DocumentAnalysisResponse(
            document_id=request.document_id,
            document_type=DocumentType.UNKNOWN,
            extracted_text=extracted_text,
            confidence=0.3,
            warnings=[f"Analysis failed: {str(e)}"]
        )
