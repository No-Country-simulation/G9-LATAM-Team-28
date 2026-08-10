# Imagen base oficial de Python 3.12 (requerida por numpy 2.5.1 y dependencias modernas)
FROM python:3.12-slim

# Evitar que Python escriba archivos .pyc y permitir que la salida de logs se envíe directamente al terminal
ENV PYTHONDONTWRITEBYTECODE=1 \
    PYTHONUNBUFFERED=1

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Instalar dependencias del sistema requeridas (incluyendo soporte OCR opcional si se desea)
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    tesseract-ocr \
    tesseract-ocr-spa \
    && rm -rf /var/lib/apt/lists/*

# Copiar requirements.txt e instalar dependencias de Python
COPY requirements.txt .
RUN pip install --no-cache-dir --upgrade pip && \
    pip install --no-cache-dir -r requirements.txt

# Copiar el código de la aplicación y el modelo entrenado
COPY main.py .
COPY modelo_clasificador_cursos_udemy.pkl .

# Exponer el puerto en el que escucha FastAPI
EXPOSE 8000

# Comando para iniciar el servidor Uvicorn
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
