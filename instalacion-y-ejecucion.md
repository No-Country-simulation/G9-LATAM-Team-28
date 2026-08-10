# Guía de Instalación y Ejecución


## Aplicación que expone el modelo ML via API
### 1. Requisitos Previos
Antes de comenzar, asegúrate de contar con:
* **Python 3.10** o superior instalado en el sistema.
* **Git** instalado.
* **Pip** (gestor de paquetes de Python) actualizado.
* *(Opcional)* **Tesseract OCR** instalado en el sistema operativo si deseas procesar texto desde imágenes (`.png`, `.jpg`).

### 2. Preparación del Entorno
#### Paso 2.1: Clonar o posicionarse en el proyecto
Abre una terminal y navega hasta la carpeta raíz del proyecto:
```bash
cd /ruta/al/proyecto/G9-LATAM-Team-28
```
#### Paso 2.2: Crear un entorno virtual (Recomendado)
Crear un entorno virtual aísla las dependencias del proyecto evitando conflictos globales:

* **En Linux / macOS:**
  ```bash
  python3 -m venv .venv
  ```
* **En Windows:**
  ```bash
  python -m venv .venv
  ```

#### Paso 2.3: Activar el entorno virtual

* **En Linux / macOS:**
  ```bash
  source .venv/bin/activate
  ```
* **En Windows (PowerShell):**
  ```powershell
  .venv\Scripts\Activate.ps1
  ```
* **En Windows (CMD):**
  ```cmd
  .venv\Scripts\activate.bat
  ```

### 3. Instalación de Dependencias

Con el entorno virtual activado, instala todas las librerías necesarias especificadas en `requirements.txt`:

```bash
pip install --upgrade pip
pip install -r requirements.txt
```

> **Nota:** Verifica que el archivo del modelo entrenado `modelo_clasificador_cursos_udemy.pkl` se encuentre en la raíz del proyecto, ya que `main.py` lo requiere al iniciar.


### 4. Ejecución del Servidor

Para iniciar la aplicación, ejecuta **Uvicorn** apuntando a la instancia `app` en `main.py`:

```bash
uvicorn main:app --reload
```

#### Opciones de ejecución comunes:
* **`--reload`**: Habilita el reinicio automático del servidor cuando detecta cambios en el código (ideal para desarrollo).
* **`--port 8000`**: Especifica el puerto (por defecto es 8000).
* **`--host 0.0.0.0`**: Permite recibir conexiones desde cualquier interfaz de red/dispositivo externo.

```bash
# Ejemplo para producción o acceso en red local:
uvicorn main:app --host 0.0.0.0 --port 8000
```

### 5. Acceso a la Aplicación

Una vez iniciado el servidor, podrás acceder a las siguientes interfaces desde tu navegador:

| Interfaz | URL | Descripción |
| :--- | :--- | :--- |
| 🌐 **Frontend Web (TechMind)** | [http://127.0.0.1:8000/](http://127.0.0.1:8000/) | Interfaz gráfica interactiva con subida de archivos, historial y clasificación en tiempo real. |
| 📑 **Documentación Swagger UI** | [http://127.0.0.1:8000/docs](http://127.0.0.1:8000/docs) | Panel interactivo de FastAPI para probar y explorar todos los endpoints y esquemas. |
| 📄 **Documentación ReDoc** | [http://127.0.0.1:8000/redoc](http://127.0.0.1:8000/redoc) | Documentación técnica alternativa y estructurada en formato OpenAPI. |


### 6. Pruebas y Consumo de la API (`/classify`)

Puedes enviar solicitudes al endpoint `POST /classify` mediante formularios, archivos o peticiones HTTP:

#### Ejemplo con cURL (Texto directo):
```bash
curl -X POST "http://127.0.0.1:8000/classify" \
     -F "title=Machine Learning Specialization" \
     -F "text=Deep learning, neural networks, computer vision and NLP course."
```

#### Ejemplo con cURL (Subiendo un archivo PDF o Word):
```bash
curl -X POST "http://127.0.0.1:8000/classify" \
     -F "file=@/ruta/a/mi_documento.pdf"
```

#### Ejemplo de respuesta JSON:
```json
{
  "categoria": "Data Science",
  "confianza": "94%",
  "palabras_clave": [
    "deep",
    "learning",
    "neural",
    "networks",
    "vision",
    "nlp"
  ]
}
```
