from pydantic import BaseModel, Field
from typing import Optional
from enum import Enum


class ProductInfo(BaseModel):
    name: str
    category: str
    ingredients: Optional[str] = None
    packaging_type: Optional[str] = None
    weight_grams: Optional[int] = None
    organic: bool = False
    hs_code: Optional[str] = None


class AnalysisRequest(BaseModel):
    compliance_case_id: str
    company_country: str = Field(..., description="ISO 3166-1 alpha-2 or alpha-3 country code")
    target_country: str = Field(..., description="ISO 3166-1 alpha-2 or alpha-3 country code")
    product: ProductInfo


class RequirementStatus(str, Enum):
    REQUIRED = "REQUIRED"
    RECOMMENDED = "RECOMMENDED"
    NOT_APPLICABLE = "NOT_APPLICABLE"


class RequirementType(str, Enum):
    LEGAL = "LEGAL"
    CERTIFICATION = "CERTIFICATION"
    LABELING = "LABELING"
    HEALTH_SAFETY = "HEALTH_SAFETY"
    CUSTOMS = "CUSTOMS"
    TRACEABILITY = "TRACEABILITY"
    PACKAGING = "PACKAGING"
    OTHER = "OTHER"


class SourceReference(BaseModel):
    title: str
    url: Optional[str] = None
    article: Optional[str] = None
    regulation_number: Optional[str] = None


class RequirementItem(BaseModel):
    code: str
    type: RequirementType
    title: str
    status: RequirementStatus
    legal_basis: Optional[str] = None
    source_url: Optional[str] = None
    reason: str
    evidence_required: list[str] = []
    sources: list[SourceReference] = []
    confidence: float = Field(..., ge=0.0, le=1.0)


class ProductClassification(BaseModel):
    category: str
    sub_category: str
    suggested_hs_code: Optional[str] = None
    confidence: float = Field(..., ge=0.0, le=1.0)


class AnalysisResponse(BaseModel):
    compliance_case_id: str
    product_classification: ProductClassification
    requirements: list[RequirementItem]
    missing_information: list[str] = []
    warnings: list[str] = []
    overall_confidence: float = Field(..., ge=0.0, le=1.0)
    analysis_summary: str
