import os
from typing import List
from pydantic import BaseModel


class Settings(BaseModel):
    APP_TITLE: str = "TechMind - Organizador Inteligente de Contenido"
    APP_DESCRIPTION: str = "Backend para la plataforma de clasificación de texto con NLP."
    APP_VERSION: str = "2.0.0"
    MODEL_PATH: str = os.getenv("MODEL_PATH", "modelo_clasificador_cursos_udemy.pkl")
    MAX_UPLOAD_SIZE_MB: int = int(os.getenv("MAX_UPLOAD_SIZE_MB", "20"))
    CORS_ORIGINS: List[str] = ["*"]


settings = Settings()
