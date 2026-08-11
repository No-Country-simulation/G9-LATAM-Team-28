import io
from fastapi import HTTPException
import pypdf
import docx
from PIL import Image
from core.config import settings

try:
    import pytesseract
    HAS_TESSERACT = True
except ImportError:
    HAS_TESSERACT = False


def extraer_texto_de_archivo(file_bytes: bytes, filename: str) -> str:
    """Extrae el contenido de texto plano desde archivos TXT, PDF, DOCX e imágenes (OCR)."""
    max_bytes = settings.MAX_UPLOAD_SIZE_MB * 1024 * 1024
    if len(file_bytes) > max_bytes:
        raise HTTPException(
            status_code=400,
            detail=f"El archivo excede el tamaño máximo permitido de {settings.MAX_UPLOAD_SIZE_MB}MB."
        )

    extension = filename.split(".")[-1].lower()
    texto_extraido = ""

    if extension == "txt":
        texto_extraido = file_bytes.decode("utf-8", errors="ignore")

    elif extension == "pdf":
        try:
            pdf_reader = pypdf.PdfReader(io.BytesIO(file_bytes))
            paginas_texto = [page.extract_text() for page in pdf_reader.pages if page.extract_text()]
            texto_extraido = "\n".join(paginas_texto)
        except Exception as e:
            raise HTTPException(status_code=400, detail=f"Error al leer el archivo PDF: {str(e)}")

    elif extension == "docx":
        try:
            doc = docx.Document(io.BytesIO(file_bytes))
            texto_extraido = "\n".join([p.text for p in doc.paragraphs if p.text])
        except Exception as e:
            raise HTTPException(status_code=400, detail=f"Error al leer el archivo DOCX: {str(e)}")

    elif extension in ["png", "jpg", "jpeg"]:
        if not HAS_TESSERACT:
            raise HTTPException(
                status_code=400,
                detail="Soporte OCR no disponible. Requiere la biblioteca 'pytesseract' y motor Tesseract en el sistema."
            )
        try:
            imagen = Image.open(io.BytesIO(file_bytes))
            texto_extraido = pytesseract.image_to_string(imagen)
        except Exception as e:
            raise HTTPException(status_code=400, detail=f"Error al procesar OCR en la imagen: {str(e)}")
    else:
        raise HTTPException(
            status_code=400,
            detail=f"Formato '.{extension}' no soportado. Formatos válidos: TXT, PDF, DOCX, PNG, JPG."
        )

    return texto_extraido.strip()
