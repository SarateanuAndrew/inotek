from pydantic import BaseModel
from typing import Optional
from datetime import date
from enum import Enum


class DocumentType(str, Enum):
    ISO_CERTIFICATE = "ISO_CERTIFICATE"
    HEALTH_CERTIFICATE = "HEALTH_CERTIFICATE"
    LAB_TEST = "LAB_TEST"
    ORGANIC_CERTIFICATE = "ORGANIC_CERTIFICATE"
    COMPANY_REGISTRATION = "COMPANY_REGISTRATION"
    HACCP_CERTIFICATE = "HACCP_CERTIFICATE"
    EXPORT_LICENSE = "EXPORT_LICENSE"
    PHYTOSANITARY = "PHYTOSANITARY"
    UNKNOWN = "UNKNOWN"


class DocumentAnalysisRequest(BaseModel):
    document_id: str
    company_id: str
    file_content_base64: str
    mime_type: str = "application/pdf"


class DocumentAnalysisResponse(BaseModel):
    document_id: str
    document_type: DocumentType
    issuer: Optional[str] = None
    company_name: Optional[str] = None
    certificate_number: Optional[str] = None
    issued_date: Optional[date] = None
    expiry_date: Optional[date] = None
    scope: Optional[str] = None
    extracted_text: Optional[str] = None
    confidence: float
    warnings: list[str] = []
