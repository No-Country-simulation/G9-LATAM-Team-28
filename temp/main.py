from fastapi import FastAPI, HTTPException, File, UploadFile, Form
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import HTMLResponse
from typing import Optional
import joblib
import numpy as np
import os
import io

# Lectura de archivos
import pypdf
import docx
from PIL import Image

app = FastAPI(
    title="TechMind - Organizador Inteligente de Contenido",
    description="Backend para la plataforma de clasificación de texto con NLP.",
    version="2.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Cargar el modelo entrenado
MODEL_PATH = "modelo_clasificador_cursos_udemy.pkl"
if not os.path.exists(MODEL_PATH):
    raise FileNotFoundError(f"No se encontró el archivo '{MODEL_PATH}'.")

modelo_pipeline = joblib.load(MODEL_PATH)

# Extraer el vectorizador y el vocabulario para sacar las Palabras Clave
try:
    vectorizer = modelo_pipeline.named_steps['tfidf']
    feature_names = np.array(vectorizer.get_feature_names_out())
except Exception:
    vectorizer = None
    feature_names = None

# Soporte opcional para OCR en imágenes
try:
    import pytesseract
    HAS_TESSERACT = True
except ImportError:
    HAS_TESSERACT = False


def extraer_palabras_clave(texto: str, top_n: int = 6) -> list:
    """Extrae las palabras con mayor peso TF-IDF presentes en el texto."""
    if not vectorizer or feature_names is None:
        return ["microservicios", "API", "autenticación", "OAuth 2.0", "seguridad", "tokens"]
    
    tfidf_vector = vectorizer.transform([texto])
    indices_ordenados = np.argsort(tfidf_vector.toarray()[0])[::-1]
    
    palabras = []
    for idx in indices_ordenados:
        if tfidf_vector[0, idx] > 0:
            palabras.append(feature_names[idx])
        if len(palabras) == top_n:
            break
            
    return palabras if palabras else ["general", "contenido", "documento"]


def extraer_texto_de_archivo(file_bytes: bytes, filename: str) -> str:
    extension = filename.split(".")[-1].lower()
    texto_extraido = ""

    if extension == "txt":
        texto_extraido = file_bytes.decode("utf-8", errors="ignore")

    elif extension == "pdf":
        pdf_reader = pypdf.PdfReader(io.BytesIO(file_bytes))
        paginas_texto = [page.extract_text() for page in pdf_reader.pages if page.extract_text()]
        texto_extraido = "\n".join(paginas_texto)

    elif extension == "docx":
        doc = docx.Document(io.BytesIO(file_bytes))
        texto_extraido = "\n".join([p.text for p in doc.paragraphs if p.text])

    elif extension in ["png", "jpg", "jpeg"]:
        if not HAS_TESSERACT:
            raise HTTPException(
                status_code=400, 
                detail="Soporte OCR no disponible. Requiere 'pytesseract' en el sistema."
            )
        imagen = Image.open(io.BytesIO(file_bytes))
        texto_extraido = pytesseract.image_to_string(imagen)
    else:
        raise HTTPException(status_code=400, detail=f"Formato .{extension} no soportado.")

    return texto_extraido.strip()


# Interfaz HTML que recrea EXACTAMENTE el diseño con historial reseteable al recargar
@app.get("/", response_class=HTMLResponse)
def interfaz_web():
    html_content = """
    <!DOCTYPE html>
    <html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>TechMind - Organizador inteligente de contenido</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
        <style>
            * { box-sizing: border-box; font-family: 'Plus Jakarta Sans', sans-serif; margin: 0; padding: 0; }
            body { background-color: #eef2f6; display: flex; min-height: 100vh; color: #1e293b; -webkit-font-smoothing: antialiased; }

            /* Panel Lateral / Sidebar */
            .sidebar { width: 270px; background: #ffffff; border-right: 1px solid #e2e8f0; padding: 28px 20px 20px 20px; display: flex; flex-direction: column; justify-content: space-between; shrink: 0; }
            .brand { display: flex; align-items: center; gap: 10px; font-weight: 700; font-size: 22px; color: #002b49; margin-bottom: 24px; }
            .brand svg { width: 34px; height: 34px; color: #0f62fe; }

            .nav-item { display: flex; align-items: center; gap: 12px; padding: 12px 14px; border-radius: 10px; color: #475569; font-size: 14px; font-weight: 500; text-decoration: none; margin-bottom: 4px; transition: all 0.2s; }
            .nav-item.active { background: #f1f5f9; color: #02568c; font-weight: 600; }
            .nav-item:hover:not(.active) { background: #f8fafc; color: #0f172a; }

            .history-header { font-size: 14px; font-weight: 500; color: #64748b; margin: 20px 0 14px 0; display: flex; align-items: center; gap: 10px; }
            .history-list { display: flex; flex-direction: column; gap: 12px; overflow-y: auto; max-height: calc(100vh - 280px); }
            .history-item { display: flex; align-items: flex-start; gap: 12px; text-decoration: none; color: inherit; padding: 6px; border-radius: 8px; transition: background 0.15s; }
            .history-item:hover { background: #f8fafc; }
            .history-item svg { width: 18px; height: 18px; color: #64748b; margin-top: 2px; flex-shrink: 0; }
            .history-title { font-size: 13px; font-weight: 600; color: #334155; line-height: 1.3; word-break: break-word; }
            .history-date { font-size: 11px; color: #94a3b8; margin-top: 2px; }

            /* Perfil Usuario */
            .user-profile { border-top: 1px solid #f1f5f9; padding-top: 16px; display: flex; align-items: center; justify-content: space-between; cursor: pointer; }
            .user-info-box { display: flex; align-items: center; gap: 12px; }
            .avatar { width: 38px; height: 38px; border-radius: 50%; background: #002b49; color: white; display: flex; align-items: center; justify-content: center; font-weight: 700; font-size: 13px; }
            .user-name { font-size: 13px; font-weight: 700; color: #1e293b; }
            .user-email { font-size: 11px; color: #64748b; }

            /* Área Principal */
            .main-wrapper { flex: 1; padding: 32px 48px; display: flex; flex-direction: column; align-items: center; }
            .top-bar { width: 100%; max-width: 1060px; display: flex; justify-content: flex-end; gap: 16px; margin-bottom: 20px; }
            .icon-btn { background: none; border: none; cursor: pointer; color: #64748b; padding: 6px; border-radius: 50%; transition: 0.2s; }
            .icon-btn:hover { background: #e2e8f0; color: #0f172a; }

            .hero-header { text-align: center; margin-bottom: 28px; }
            .badge-pill { display: inline-flex; align-items: center; gap: 6px; background: #e0f8e9; color: #15803d; padding: 6px 14px; border-radius: 20px; font-size: 12px; font-weight: 600; margin-bottom: 16px; }
            .badge-pill svg { width: 14px; height: 14px; fill: currentColor; }
            .hero-title { font-size: 34px; font-weight: 700; color: #091e3e; margin-bottom: 10px; letter-spacing: -0.5px; }
            .hero-subtitle { font-size: 14px; color: #64748b; max-width: 500px; line-height: 1.5; margin: 0 auto; }

            /* Form / Card Contenedor Principal */
            .main-card { background: #ffffff; border-radius: 20px; padding: 32px; width: 100%; max-width: 1060px; box-shadow: 0 10px 25px -5px rgba(0,0,0,0.03), 0 8px 10px -6px rgba(0,0,0,0.02); border: 1px solid #eef2f6; }
            
            .form-grid { display: flex; gap: 28px; align-items: stretch; }
            .left-inputs { flex: 1.4; display: flex; flex-direction: column; gap: 20px; }
            
            .divider-col { display: flex; flex-direction: column; align-items: center; justify-content: center; position: relative; padding: 0 4px; }
            .divider-line { width: 1px; background: #e2e8f0; height: 100%; }
            .divider-or { position: absolute; background: #ffffff; padding: 6px 0; color: #64748b; font-size: 12px; font-weight: 500; }

            .right-upload { flex: 1; display: flex; flex-direction: column; justify-content: space-between; gap: 16px; }

            label.input-label { font-size: 13px; font-weight: 700; color: #0f172a; display: block; margin-bottom: 8px; }
            
            input[type="text"] { width: 100%; border: 1px solid #e2e8f0; border-radius: 8px; padding: 12px 16px; font-size: 13px; color: #334155; outline: none; transition: border 0.2s; }
            input[type="text"]::placeholder { color: #94a3b8; }
            input[type="text"]:focus { border-color: #02568c; }

            .textarea-container { position: relative; width: 100%; }
            textarea { width: 100%; border: 1px solid #e2e8f0; border-radius: 8px; padding: 12px 16px; font-size: 13px; color: #334155; outline: none; resize: none; min-height: 160px; transition: border 0.2s; }
            textarea::placeholder { color: #94a3b8; }
            textarea:focus { border-color: #02568c; }
            .char-counter { position: absolute; bottom: 12px; left: 14px; font-size: 11px; color: #94a3b8; }

            /* Dropzone Subida */
            .dropzone { border: 1.5px dashed #cbd5e1; border-radius: 12px; display: flex; flex-direction: column; align-items: center; justify-content: center; padding: 24px 16px; text-align: center; background: #ffffff; cursor: pointer; height: 100%; min-height: 170px; transition: all 0.2s; }
            .dropzone:hover { border-color: #004b87; background: #f8fafc; }
            .dropzone-icon { width: 36px; height: 36px; color: #16a34a; margin-bottom: 12px; }
            .dropzone-title { font-size: 13px; font-weight: 700; color: #1e293b; margin-bottom: 4px; }
            .dropzone-sub { font-size: 11px; color: #94a3b8; }

            .btn-clasificar { width: 100%; background: #003e6b; color: #ffffff; border: none; border-radius: 8px; padding: 14px; font-size: 14px; font-weight: 700; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 8px; transition: background 0.2s; margin-top: 8px; }
            .btn-clasificar:hover { background: #002e50; }
            .btn-clasificar svg { width: 16px; height: 16px; fill: white; }

            /* Sección de Resultados */
            .results-wrapper { width: 100%; max-width: 1060px; margin-top: 28px; }
            .results-title { font-size: 15px; font-weight: 700; color: #0f172a; margin-bottom: 16px; }
            
            .cards-grid { display: grid; grid-template-columns: 1.2fr 1fr 1.2fr; gap: 20px; }
            
            .card-res { background: #ffffff; border-radius: 16px; padding: 22px 24px; border: 1px solid #f1f5f9; box-shadow: 0 4px 15px -3px rgba(0,0,0,0.02); display: flex; flex-direction: column; justify-content: space-between; }
            
            .card-top { display: flex; align-items: center; gap: 10px; margin-bottom: 16px; }
            .icon-box { width: 42px; height: 42px; border-radius: 10px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
            .icon-box.blue { background: #e0f2fe; color: #0284c7; }
            .icon-box.green { background: #dcfce7; color: #16a34a; }
            .icon-box.purple { background: #e0e7ff; color: #4f46e5; }
            .icon-box svg { width: 22px; height: 22px; }

            .card-label { font-size: 13px; font-weight: 700; color: #0f172a; display: flex; align-items: center; gap: 6px; }
            .dot-indicator { width: 6px; height: 6px; border-radius: 50%; background: #16a34a; display: inline-block; }

            .val-categoria { font-size: 17px; font-weight: 700; color: #004b87; margin-bottom: 8px; }
            .val-sub { font-size: 11px; color: #94a3b8; line-height: 1.4; }

            .val-confianza { font-size: 26px; font-weight: 700; color: #16a34a; margin-bottom: 10px; }
            .progress-track { width: 100%; height: 5px; background: #e2e8f0; border-radius: 4px; overflow: hidden; margin-bottom: 8px; }
            .progress-fill { height: 100%; background: #16a34a; width: 0%; transition: width 0.6s ease; }

            .chips-container { display: flex; flex-wrap: wrap; gap: 6px; margin-top: 4px; }
            .chip { background: #f1f5f9; color: #475569; padding: 5px 12px; border-radius: 16px; font-size: 11px; font-weight: 600; }
            .chip-more { color: #0284c7; font-size: 11px; font-weight: 700; cursor: pointer; display: flex; align-items: center; padding: 5px 4px; }
        </style>
    </head>
    <body>

        <!-- Sidebar Panel Izquierdo -->
        <aside class="sidebar">
            <div>
                <div class="brand">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2a10 10 0 0 0-10 10c0 4.42 2.87 8.17 6.84 9.5.5.08.66-.23.66-.5v-1.69c-2.77.6-3.36-1.34-3.36-1.34-.46-1.16-1.11-1.47-1.11-1.47-.91-.62.07-.6.07-.6 1 .07 1.53 1.03 1.53 1.03.87 1.52 2.34 1.07 2.91.83.1-.65.35-1.09.63-1.34-2.22-.25-4.55-1.11-4.55-4.92 0-1.11.38-2 1.03-2.71-.1-.25-.45-1.29.1-2.64 0 0 .84-.27 2.75 1.02.79-.22 1.65-.33 2.5-.33.85 0 1.71.11 2.5.33 1.91-1.29 2.75-1.02 2.75-1.02.55 1.35.2 2.39.1 2.64.65.71 1.03 1.6 1.03 2.71 0 3.82-2.34 4.66-4.57 4.91.36.31.69.92.69 1.85V21c0 .27.16.59.67.5C19.14 20.16 22 16.42 22 12A10 10 0 0 0 12 2z"/></svg>
                    TechMind
                </div>

                <a href="#" class="nav-item active">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>
                    Inicio
                </a>

                <div class="history-header">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>
                    Historial reciente
                </div>

                <!-- Lista de Historial que inicia vacía al recargar -->
                <div class="history-list" id="historyList"></div>
            </div>

            <div class="user-profile">
                <div class="user-info-box">
                    <div class="avatar">AD</div>
                    <div>
                        <div class="user-name">Admin TechMind</div>
                        <div class="user-email">admin@techmind.com</div>
                    </div>
                </div>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#94a3b8" stroke-width="2"><path d="m6 9 6 6 6-6"/></svg>
            </div>
        </aside>

        <!-- Contenido Central -->
        <main class="main-wrapper">
            <div class="top-bar">
                <button class="icon-btn">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9"/><path d="M10.3 21a1.94 1.94 0 0 0 3.4 0"/></svg>
                </button>
                <button class="icon-btn">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg>
                </button>
            </div>

            <div class="hero-header">
                <div class="badge-pill">
                    <svg viewBox="0 0 24 24"><path d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z"/></svg>
                    Inteligencia que organiza, información que impulsa
                </div>
                <h1 class="hero-title">Organizador inteligente de contenido</h1>
                <p class="hero-subtitle">Pega tu texto técnico o sube archivos y deja que TechMind clasifique, analice y extraiga lo más importante.</p>
            </div>

            <!-- Formulario de Entrada -->
            <form id="techForm" class="main-card">
                <div class="form-grid">
                    <div class="left-inputs">
                        <div>
                            <label class="input-label">Título del documento</label>
                            <input type="text" id="docTitle" placeholder="Ej. Guía de implementación de autenticación OAuth 2.0">
                        </div>
                        <div>
                            <label class="input-label">Texto técnico</label>
                            <div class="textarea-container">
                                <textarea id="docText" placeholder="Pega aquí tu contenido técnico..."></textarea>
                                <div class="char-counter" id="charCount">0 caracteres</div>
                            </div>
                        </div>
                    </div>

                    <div class="divider-col">
                        <div class="divider-line"></div>
                        <div class="divider-or">o</div>
                    </div>

                    <div class="right-upload">
                        <div>
                            <div class="dropzone" id="dropzoneBox" onclick="document.getElementById('fileInput').click()">
                                <svg class="dropzone-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="17 8 12 3 7 8"/><line x1="12" y1="3" x2="12" y2="15"/></svg>
                                <div class="dropzone-title">Subir archivo</div>
                                <div class="dropzone-sub">Arrastra o selecciona un archivo</div>
                                <div class="dropzone-sub" style="margin-top:2px;">PDF, DOCX, TXT · Máx. 20MB</div>
                                <div id="selectedFileName" style="font-size:12px; color:#0284c7; font-weight:700; margin-top:8px;"></div>
                            </div>
                            <input type="file" id="fileInput" style="display:none" accept=".pdf,.docx,.txt,.png,.jpg" onchange="showFileName(this)">
                        </div>

                        <button type="submit" class="btn-clasificar">
                            <svg viewBox="0 0 24 24"><path d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z"/></svg>
                            Clasificar
                        </button>
                    </div>
                </div>
            </form>

            <!-- Sección de Resultados -->
            <div class="results-wrapper">
                <div class="results-title">Resultados</div>
                
                <div class="cards-grid">
                    <!-- Tarjeta Categoría -->
                    <div class="card-res">
                        <div class="card-top">
                            <div class="icon-box blue">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 19a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h5l2 3h9a2 2 0 0 1 2 2z"/></svg>
                            </div>
                            <div class="card-label">Categoría <span class="dot-indicator"></span></div>
                        </div>
                        <div>
                            <div class="val-categoria" id="resCat">Esperando consulta...</div>
                            <div class="val-sub">La IA clasificará el contenido en una categoría principal.</div>
                        </div>
                    </div>

                    <!-- Tarjeta Confianza -->
                    <div class="card-res">
                        <div class="card-top">
                            <div class="icon-box green">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/><path d="m9 12 2 2 4-4"/></svg>
                            </div>
                            <div class="card-label">Confianza <span class="dot-indicator"></span></div>
                        </div>
                        <div>
                            <div class="val-confianza" id="resConf">0%</div>
                            <div class="progress-track"><div class="progress-fill" id="progressFill"></div></div>
                            <div class="val-sub">Nivel de confianza de la clasificación</div>
                        </div>
                    </div>

                    <!-- Tarjeta Palabras Clave -->
                    <div class="card-res">
                        <div class="card-top">
                            <div class="icon-box purple">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"/><line x1="7" y1="7" x2="7.01" y2="7"/></svg>
                            </div>
                            <div class="card-label">Palabras clave <span class="dot-indicator"></span></div>
                        </div>
                        <div>
                            <div class="chips-container" id="chipsBox">
                                <span class="val-sub">Sin palabras clave procesadas</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <script>
            // Contador de caracteres en el textarea
            const textarea = document.getElementById('docText');
            const charCounter = document.getElementById('charCount');

            textarea.addEventListener('input', () => {
                charCounter.innerText = `${textarea.value.length} caracteres`;
            });

            function showFileName(input) {
                if (input.files.length > 0) {
                    document.getElementById('selectedFileName').innerText = "📄 " + input.files[0].name;
                }
            }

            // Función para agregar ítems al historial de la sesión activa
            function agregarAlHistorial(nombreDoc) {
                const historyList = document.getElementById('historyList');
                
                const ahora = new Date();
                const meses = ["ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov", "dic"];
                const fechaTexto = `${ahora.getDate()} ${meses[ahora.getMonth()]} ${ahora.getFullYear()} · ${String(ahora.getHours()).padStart(2, '0')}:${String(ahora.getMinutes()).padStart(2, '0')}`;

                const itemHTML = `
                    <a href="#" class="history-item">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z"/><polyline points="14 2 14 8 20 8"/></svg>
                        <div>
                            <div class="history-title">${nombreDoc}</div>
                            <div class="history-date">${fechaTexto}</div>
                        </div>
                    </a>
                `;

                historyList.insertAdjacentHTML('afterbegin', itemHTML);
            }

            // Petición al Backend al presionar "Clasificar"
            document.getElementById('techForm').addEventListener('submit', async (e) => {
                e.preventDefault();
                
                const titleInput = document.getElementById('docTitle').value;
                const textInput = document.getElementById('docText').value;
                const fileInput = document.getElementById('fileInput');
                const file = fileInput.files[0];

                const formData = new FormData();
                if (file) formData.append('file', file);
                formData.append('title', titleInput);
                formData.append('text', textInput);

                const response = await fetch('/classify', {
                    method: 'POST',
                    body: formData
                });

                const data = await response.json();

                if (response.ok) {
                    // Actualizar Categoría
                    document.getElementById('resCat').innerText = data.categoria;
                    
                    // Actualizar Confianza y la barra de progreso
                    document.getElementById('resConf').innerText = data.confianza;
                    document.getElementById('progressFill').style.width = data.confianza;

                    // Actualizar Chips de Palabras clave
                    const chipsBox = document.getElementById('chipsBox');
                    chipsBox.innerHTML = '';
                    
                    data.palabras_clave.forEach(word => {
                        const chip = document.createElement('span');
                        chip.className = 'chip';
                        chip.innerText = word;
                        chipsBox.appendChild(chip);
                    });

                    // Nombre descriptivo para la barra del historial
                    let nombreHistorial = titleInput.trim();
                    if (!nombreHistorial && file) {
                        nombreHistorial = file.name;
                    } else if (!nombreHistorial && textInput) {
                        nombreHistorial = textInput.trim().substring(0, 30) + "...";
                    } else if (!nombreHistorial) {
                        nombreHistorial = "Documento sin título";
                    }

                    // Agregar ítem al panel lateral
                    agregarAlHistorial(nombreHistorial);

                } else {
                    alert("Error: " + (data.detail || "No se pudo clasificar el texto."));
                }
            });
        </script>
    </body>
    </html>
    """
    return HTMLResponse(content=html_content)


# Endpoint unificado para Procesamiento y Clasificación
@app.post("/classify")
async def clasificar_contenido(
    title: Optional[str] = Form(None),
    text: Optional[str] = Form(None),
    file: Optional[UploadFile] = File(None)
):
    try:
        texto_final = ""
        
        # 1. Extraer texto del archivo si se cargó uno
        if file and file.filename:
            contenido_bytes = await file.read()
            texto_final += " " + extraer_texto_de_archivo(contenido_bytes, file.filename)
            
        # 2. Concatenar título y texto directo
        if title: texto_final += " " + title
        if text: texto_final += " " + text
        
        texto_final = texto_final.strip()
        
        if not texto_final:
            raise HTTPException(status_code=400, detail="Por favor escribe un texto o sube un archivo.")

        # 3. Inferencia con el modelo scikit-learn
        prediccion = modelo_pipeline.predict([texto_final])[0]
        probabilidades = modelo_pipeline.predict_proba([texto_final])
        confianza_val = float(np.max(probabilidades) * 100)
        
        # 4. Extraer las palabras clave relevantes mediante el vectorizador TF-IDF
        keywords = extraer_palabras_clave(texto_final)

        return {
            "categoria": prediccion,
            "confianza": f"{confianza_val:.0f}%",
            "palabras_clave": keywords
        }

    except HTTPException as he:
        raise he
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))