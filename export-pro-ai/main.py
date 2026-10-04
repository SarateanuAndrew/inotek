import logging
from fastapi import FastAPI, Request
from fastapi.exceptions import RequestValidationError
from fastapi.responses import JSONResponse
from fastapi.middleware.cors import CORSMiddleware

logging.basicConfig(level=logging.WARNING)
logger = logging.getLogger(__name__)
from app.api import compliance, documents
from app.api.roadmap import router as roadmap_router
from app.api.certifications import router as certifications_router
from app.core.config import get_settings

settings = get_settings()

app = FastAPI(
    title="Export Pro AI Service",
    description="AI-powered compliance analysis for EU market access",
    version="0.1.0"
)

@app.exception_handler(RequestValidationError)
async def validation_exception_handler(request: Request, exc: RequestValidationError):
    body = await request.body()
    logger.warning("422 VALIDATION ERROR on %s", request.url.path)
    logger.warning("RAW BODY: %s", body)
    logger.warning("ERRORS: %s", exc.errors())
    return JSONResponse(status_code=422, content={"detail": exc.errors()})


app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:8080", "http://localhost:3000"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(compliance.router)
app.include_router(documents.router)
app.include_router(roadmap_router)
app.include_router(certifications_router)


@app.get("/health")
async def health():
    return {"status": "ok", "service": "export-pro-ai"}


if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
