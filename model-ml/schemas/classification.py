from typing import List
from pydantic import BaseModel, Field


class ClassifyResponse(BaseModel):
    categoria: str = Field(
        ...,
        description="Categoría predicha por el modelo NLP de clasificación",
        example="Desarrollo Web"
    )
    confianza: str = Field(
        ...,
        description="Porcentaje de confianza asociado a la predicción del modelo",
        example="95%"
    )
    palabras_clave: List[str] = Field(
        ...,
        description="Lista de palabras clave de mayor relevancia TF-IDF presentes en el documento",
        example=["microservicios", "API", "seguridad"]
    )


class HealthResponse(BaseModel):
    status: str = Field(
        ...,
        description="Estado de salud general del servicio",
        example="healthy"
    )
    model_loaded: bool = Field(
        ...,
        description="Indica si el modelo de Machine Learning se encuentra cargado y listo para inferencias",
        example=True
    )
    version: str = Field(
        ...,
        description="Versión actual de la API",
        example="2.0.0"
    )

