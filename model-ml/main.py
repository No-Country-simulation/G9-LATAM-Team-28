from contextlib import asynccontextmanager
import logging
from typing import Optional
from pathlib import Path

from fastapi import FastAPI, HTTPException, File, UploadFile, Form
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import HTMLResponse, FileResponse
from starlette.concurrency import run_in_threadpool

from core.config import settings
from schemas.classification import ClassifyResponse, HealthResponse
from services.document_service import extraer_texto_de_archivo
from services.ml_service import ml_service


# Configuración básica de logging
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(name)s - %(levelname)s - %(message)s"
)
logger = logging.getLogger("main")


@asynccontextmanager
async def lifespan(app: FastAPI):
    """Manejador de ciclo de vida para cargar artefactos al inicio."""
    logger.info("Iniciando aplicación y cargando modelos de Machine Learning...")
    try:
        ml_service.cargar_modelo(settings.MODEL_PATH)
    except Exception as e:
        logger.error(f"Error crítico al cargar el modelo ML: {e}")
        raise e
    yield
    logger.info("Finalizando aplicación...")


app = FastAPI(
    title=settings.APP_TITLE,
    description=settings.APP_DESCRIPTION,
    version=settings.APP_VERSION,
    lifespan=lifespan
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.CORS_ORIGINS,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.get(
    "/",
    response_class=HTMLResponse,
    summary="Interfaz de usuario",
    description="Sirve la interfaz web interactiva de TechMind."
)
async def interfaz_web():
    template_path = Path(__file__).parent / "templates" / "index.html"
    if not template_path.exists():
        raise HTTPException(status_code=404, detail="Plantilla de interfaz no encontrada.")
    return FileResponse(template_path)


@app.get(
    "/health",
    response_model=HealthResponse,
    summary="Estado de salud del servicio (Health Check)",
    description="Devuelve la disponibilidad general de la API y el estado del modelo ML."
)
async def health_check() -> HealthResponse:
    return HealthResponse(
        status="healthy",
        model_loaded=ml_service.esta_cargado,
        version=settings.APP_VERSION
    )


@app.post(
    "/classify",
    response_model=ClassifyResponse,
    summary="Clasificar texto y archivos",
    description="Procesa texto directo y/o archivos adjuntos (TXT, PDF, DOCX, imágenes) para clasificarlos."
)
async def clasificar_contenido(
    title: Optional[str] = Form(None),
    text: Optional[str] = Form(None),
    file: Optional[UploadFile] = File(None)
) -> ClassifyResponse:
    try:
        texto_final = ""

        # 1. Extraer texto del archivo si se envió uno
        if file and file.filename:
            contenido_bytes = await file.read()
            if contenido_bytes:
                texto_archivo = await run_in_threadpool(
                    extraer_texto_de_archivo, contenido_bytes, file.filename
                )
                texto_final += " " + texto_archivo

        # 2. Concatenar título y texto directo
        if title:
            texto_final += " " + title.strip()
        if text:
            texto_final += " " + text.strip()

        texto_final = texto_final.strip()

        if not texto_final:
            raise HTTPException(
                status_code=400,
                detail="Por favor escribe un texto o sube un archivo para analizar."
            )

        # 3. Inferencia con el servicio ML (ejecutado en thread pool para no bloquear el Event Loop)
        resultado = await run_in_threadpool(ml_service.predecir, texto_final)

        return ClassifyResponse(
            categoria=resultado["categoria"],
            confianza=resultado["confianza"],
            palabras_clave=resultado["palabras_clave"]
        )

    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"Error inesperado procesando la solicitud: {e}", exc_info=True)
        raise HTTPException(
            status_code=500,
            detail="Ocurrió un error interno en el servidor al procesar la clasificación."
        )