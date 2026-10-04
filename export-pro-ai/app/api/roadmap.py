from fastapi import APIRouter
from app.models.roadmap import StartRoadmapRequest, StartRoadmapResponse, AnswerRequest, AnswerResponse
from app.services.roadmap_service import start_roadmap, process_answer

router = APIRouter(prefix="/v1/roadmap", tags=["roadmap"])


@router.post("/start", response_model=StartRoadmapResponse)
async def start(request: StartRoadmapRequest):
    return await start_roadmap(request)


@router.post("/answer", response_model=AnswerResponse)
async def answer(request: AnswerRequest):
    return await process_answer(request)
