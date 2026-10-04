from pydantic_settings import BaseSettings
from functools import lru_cache


class Settings(BaseSettings):
    openai_api_key: str = ""
    database_url: str = "postgresql://postgres:postgres@localhost:5432/export_pro_ai"
    java_backend_url: str = "http://localhost:8080"
    log_level: str = "INFO"
    ai_model: str = "gpt-4o-mini"
    ai_temperature: float = 0.1

    class Config:
        env_file = ".env"


@lru_cache
def get_settings() -> Settings:
    return Settings()
