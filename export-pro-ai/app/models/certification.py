from pydantic import BaseModel
from typing import List, Optional


class CertificationRecommendRequest(BaseModel):
    product_type: str
    product_category: str
    target_country: str = "RO"
    is_organic: bool = False
    is_animal_product: bool = False
    current_certifications: List[str] = []
    company_description: Optional[str] = None


class CertificationItem(BaseModel):
    code: str
    name: str
    priority: str
    description: str
    legal_basis: str
    estimated_cost: str
    estimated_time: str
    steps: List[str]


class CertificationRecommendResponse(BaseModel):
    product_type: str
    total_required: int
    total_recommended: int
    certifications: List[CertificationItem]
    summary: str
