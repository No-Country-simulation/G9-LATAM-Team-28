from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import joblib
import re
from typing import List

# 1. Inicializar la aplicación FastAPI
app = FastAPI(
    title="TechMind API - Hackathon ONE G9",
    description="API REST para clasificación inteligente de contenido técnico"
)

# 2. Configurar CORS (Permite que tu index.html se comunique con esta API sin bloqueos)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 3. Cargar el modelo entrenado
try:
    model = joblib.load('modelo_techmind.joblib')
    print("✅ Modelo 'modelo_techmind.joblib' cargado exitosamente en la API.")
except Exception as e:
    model = None
    print("⚠️ Error: No se pudo cargar el modelo. Verifica que 'modelo_techmind.joblib' exista.")

# Esqueleto de la petición recibida (Entrada)
class ContenidoEntrada(BaseModel):
    titulo: str
    texto: str

# Esqueleto de la respuesta JSON enviada (Salida requerida por la Hackatón)
class ContenidoSalida(BaseModel):
    categoria: str
    probabilidad: float
    informacion_adicional: List[str]

# Función auxiliar para extraer palabras clave
def extraer_palabras_clave(texto: str) -> List[str]:
    palabras = re.findall(r'\b[a-zA-ZáéíóúÁÉÍÓÚñÑ]{4,}\b', texto)
    ignoradas = {'este', 'esta', 'para', 'como', 'con', 'sobre', 'entre', 'donde', 'desde', 'para', 'mediante'}
    filtradas = [p.capitalize() for p in palabras if p.lower() not in ignoradas]
    return list(dict.fromkeys(filtradas))[:3]

# Endpoint obligatorio requerido por la rúbrica: POST /contenido
@app.post("/contenido", response_model=ContenidoSalida)
def clasificar_contenido(entrada: ContenidoEntrada):
    if not model:
        raise HTTPException(status_code=500, detail="El modelo de clasificación no está cargado.")
    
    if not entrada.texto.strip():
        raise HTTPException(status_code=400, detail="El texto proporcionado no puede estar vacío.")

    texto_completo = f"{entrada.titulo} {entrada.texto}"

    # Predicción de la categoría con el modelo real
    categoria_predicha = model.predict([texto_completo])[0]

    # Cálculo del nivel de confianza / probabilidad
    probabilidades = model.predict_proba([texto_completo])[0]
    clases = list(model.classes_)
    posicion = clases.index(categoria_predicha)
    confianza = round(float(probabilidades[posicion]), 2)

    # Extracción de etiquetas/palabras clave
    tags = extraer_palabras_clave(texto_completo)

    return {
        "categoria": categoria_predicha,
        "probabilidad": confianza,
        "informacion_adicional": tags
    }

# Ruta raíz de comprobación
@app.get("/")
def home():
    return {"status": "ok", "mensaje": "API TechMind operando y conectada a OCI."}