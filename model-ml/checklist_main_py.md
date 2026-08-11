# Checklist de Lineamientos y Buenas Prácticas para `main.py`

Este documento contiene la tabla de verificación de buenas prácticas, arquitectura, seguridad y rendimiento para la evaluación y refactorización de [main.py](file:///Users/greccy/Documents/Alura%20Oracle/Hackaton/G9-LATAM-Team-28/model-ml/main.py).

---

## 📋 Tabla de Verificación (Checklist)

| ID | Categoría | Lineamiento / Buena Práctica | Descripción / Justificación | Estado en `main.py` | Prioridad |
|---|---|---|---|---|---|
| **ARCH-01** | **Arquitectura** | Separación Frontend / Backend | Extraer la plantilla HTML/CSS/JS (líneas 103-465) a un archivo `templates/index.html` o servirla desde un cliente independiente para no saturar el servidor API. | ⚠️ **Por Mejorar** (HTML incrustado de >350 líneas) | 🔴 Alta |
| **ARCH-02** | **Arquitectura** | Modularidad de Funciones de Apoyo | Mover las funciones `extraer_texto_de_archivo` y `extraer_palabras_clave` a módulos separados (`services/document_service.py` y `services/nlp_service.py`). | ⚠️ **Por Mejorar** (Todo en un único archivo) | 🟡 Media |
| **ML-01** | **ML & Rendimiento** | Carga de Modelos mediante `lifespan` | Sustituir `joblib.load()` global por el gestor de contexto `@asynccontextmanager` (`lifespan`) en FastAPI para garantizar carga limpia y correcta gestión de recursos. | ⚠️ **Por Mejorar** (Carga a nivel global al importar) | 🔴 Alta |
| **ML-02** | **ML & Rendimiento** | Operaciones Asíncronas no Bloqueantes | Ejecutar la inferencia de CPU pesada (`modelo_pipeline.predict()`) y la lectura de archivos (OCR/PDF) en thread pools o mediante funciones síncronas estándar para no bloquear el Event Loop. | ⚠️ **Por Mejorar** (Mezcla de `async def` con I/O pesada síncrona) | 🔴 Alta |
| **SEC-01** | **Seguridad** | Configuración Segura de CORS | Reemplazar `allow_origins=["*"]` por una lista explícita de orígenes permitidos leída desde variables de entorno. | ⚠️ **Por Mejorar** (CORS abierto a cualquier origen) | 🔴 Alta |
| **SEC-02** | **Seguridad** | Manejo de Variables de Entorno | Centralizar rutas (`MODEL_PATH`) y configuraciones usando `pydantic-settings` o `python-dotenv`. | ⚠️ **Por Mejorar** (`MODEL_PATH` hardcodeado) | 🟡 Media |
| **SEC-03** | **Seguridad** | Validación y Límite de Archivos | Validar el tamaño máximo permitido del archivo subido en `UploadFile` y el `Content-Type` real antes de cargarlo a memoria. | ⚠️ **Por Mejorar** (Sin límite de tamaño de archivo) | 🔴 Alta |
| **SCH-01** | **Esquemas** | Uso de Pydantic `BaseModel` | Definir un modelo Pydantic de respuesta (`response_model=ClassifyResponse`) para garantizar contratos de respuesta estrictos y documentación OpenAPI limpia. | ⚠️ **Por Mejorar** (Retorna dicts genéricos) | 🟡 Media |
| **ERR-01** | **Manejo de Errores** | Logging y Manejo de Excepciones | Implementar `logging` estructurado y evitar retornar `detail=str(e)` en excepciones de 500 para no exponer tracebacks o datos sensibles del servidor. | ⚠️ **Por Mejorar** (`except Exception as e: detail=str(e)`) | 🔴 Alta |
| **ERR-02** | **Manejo de Errores** | Validación de Dependencias Opcionales | Validar disponibilidad de binarios del sistema (como Tesseract u OCR) con excepciones descriptivas. | ✅ **Cumple parcialmente** (Verifica `HAS_TESSERACT`) | 🟢 Baja |
| **DOC-01** | **Documentación** | Enriquecimiento de OpenAPI / Swagger | Incorporar metadatos `summary`, `description` y `response_model` en las rutas para autogenerar la documentación de la API. | ⚠️ **Por Mejorar** (Solo metadatos en la instancia de `FastAPI`) | 🟢 Baja |
| **COD-01** | **Calidad de Código** | Estándar PEP 8 y Tipado | Mantener tipado estricto en parámetros y funciones (`type hints`), docstrings estructurados y consistencia en el idioma del código. | ✅ **Cumple parcialmente** (Utiliza type hints y docstrings) | 🟢 Baja |

---

## 💡 Plan de Acción Recomendado

1. **Paso 1 (Inmediato):** Extraer la cadena de texto HTML a una plantilla estática (`templates/index.html`) y restringir los permisos en `CORSMiddleware`.
2. **Paso 2 (Rendimiento ML):** Migrar la carga de `joblib.load()` al evento `lifespan` de FastAPI.
3. **Paso 3 (Modularidad):** Separar en carpetas `services/`, `schemas/` y `routers/`.
