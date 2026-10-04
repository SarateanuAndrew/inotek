from pydantic import BaseModel
from typing import Optional, List, Dict


class StartRoadmapRequest(BaseModel):
    company_id: str
    company_name: str
    product_type: Optional[str] = None


class StartRoadmapResponse(BaseModel):
    session_id: str
    message: str
    axis: str


class AnswerRequest(BaseModel):
    session_id: str
    answer: str


class AxisAdvice(BaseModel):
    axis: str
    label: str
    score: int
    advice: str


class AnswerResponse(BaseModel):
    message: str
    axis: Optional[str] = None
    is_complete: bool = False
    scores: Optional[Dict[str, int]] = None
    advice: Optional[List[AxisAdvice]] = None
