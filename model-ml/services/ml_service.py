import os
import logging
from typing import List, Dict, Any, Optional
import joblib
import numpy as np

logger = logging.getLogger(__name__)


class MLModelService:
    def __init__(self) -> None:
        self.modelo_pipeline: Any = None
        self.vectorizer: Any = None
        self.feature_names: Optional[np.ndarray] = None

    @property
    def esta_cargado(self) -> bool:
        """Devuelve True si el modelo de ML está cargado en memoria."""
        return self.modelo_pipeline is not None

    def cargar_modelo(self, model_path: str) -> None:
        """Carga el modelo scikit-learn entrenado y extrae el vectorizador TF-IDF."""
        if not os.path.exists(model_path):
            logger.error(f"Archivo del modelo no encontrado en {model_path}")
            raise FileNotFoundError(f"No se encontró el archivo del modelo '{model_path}'.")

        logger.info(f"Cargando modelo ML desde {model_path}...")
        self.modelo_pipeline = joblib.load(model_path)

        try:
            self.vectorizer = self.modelo_pipeline.named_steps['tfidf']
            self.feature_names = np.array(self.vectorizer.get_feature_names_out())
            logger.info("Vectorizador TF-IDF y vocabulario inicializados con éxito.")
        except Exception as e:
            logger.warning(f"No se pudo extraer el vectorizador TF-IDF del pipeline: {e}")
            self.vectorizer = None
            self.feature_names = None

    def extraer_palabras_clave(self, texto: str, top_n: int = 6) -> List[str]:
        """Extrae las palabras con mayor peso TF-IDF presentes en el texto."""
        if not self.vectorizer or self.feature_names is None:
            return ["microservicios", "API", "autenticación", "OAuth 2.0", "seguridad", "tokens"]

        tfidf_vector = self.vectorizer.transform([texto])
        indices_ordenados = np.argsort(tfidf_vector.toarray()[0])[::-1]

        palabras = []
        for idx in indices_ordenados:
            if tfidf_vector[0, idx] > 0:
                palabras.append(str(self.feature_names[idx]))
            if len(palabras) == top_n:
                break

        return palabras if palabras else ["general", "contenido", "documento"]

    def predecir(self, texto: str) -> Dict[str, Any]:
        """Realiza la inferencia de categoría y confianza para el texto dado."""
        if self.modelo_pipeline is None:
            raise RuntimeError("El modelo ML no se encuentra cargado.")

        prediccion = str(self.modelo_pipeline.predict([texto])[0])
        probabilidades = self.modelo_pipeline.predict_proba([texto])
        confianza_val = float(np.max(probabilidades) * 100)
        keywords = self.extraer_palabras_clave(texto)

        return {
            "categoria": prediccion,
            "confianza": f"{confianza_val:.0f}%",
            "palabras_clave": keywords
        }


ml_service = MLModelService()
