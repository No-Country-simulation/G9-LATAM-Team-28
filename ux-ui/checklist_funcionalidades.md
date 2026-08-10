# Estructura de Páginas Independientes por Módulo - TechMind Enterprise

**Proyecto:** Organizador de Conocimiento Técnico | OCI & Alura Enterprise (Team 28)  
**Fecha de Actualización:** 10 de Agosto, 2026

---

## 🌐 Arquitectura de Páginas Web Independientes

La aplicación se dividió en **páginas HTML independientes para cada módulo funcional**, manteniendo un sistema de diseño homogéneo (Tailwind CSS v4, soporte para tema Claro/Oscuro, notificaciones flotantes Toasts y barra de navegación lateral unificada):

---

### 📑 1. `index.html` (Módulo 1: Inferencia & Clasificación ML)
* **Opción del Menú:** `Clasificador ML` (`<a href="index.html">`)
* **Página:** [`index.html`](file:///Users/greccy/Documents/Alura%20Oracle/Hackaton/G9-LATAM-Team-28/ux-ui/index.html)
* **Contenido:**
  * Formulario de recepción de título y texto técnico.
  * Botones de 1-clic para cargar datos de prueba (Spring Boot API, OCI Cloud, Python ML).
  * Zona Drag & Drop para carga de archivos por lotes (`.txt`, `.csv`).
  * Tarjetas de salida de inferencia ML (Categoría Predicha, Nivel de Confianza % y Etiquetas de Entidades).

---

### 🧠 2. `explicabilidad.html` (Módulo 2: Explicabilidad & API REST)
* **Opción del Menú:** `Explicabilidad & API` (`<a href="explicabilidad.html">`)
* **Página:** [`explicabilidad.html`](file:///Users/greccy/Documents/Alura%20Oracle/Hackaton/G9-LATAM-Team-28/ux-ui/explicabilidad.html)
* **Contenido:**
  * Panel de importancia de atributos (Feature Importance Weights por token `+0.48`, `+0.42`).
  * Visor modal interactivo de la respuesta JSON formateada REST API HTTP 200 OK con botón de copiado.
  * Documentación del payload HTTP Request/Response y estado del servicio (`/health`).

---

### 🔍 3. `recomendaciones.html` (Módulo 3: Descubrimiento & Búsqueda)
* **Opción del Menú:** `Búsqueda & Recomendados` (`<a href="recomendaciones.html">`)
* **Página:** [`recomendaciones.html`](file:///Users/greccy/Documents/Alura%20Oracle/Hackaton/G9-LATAM-Team-28/ux-ui/recomendaciones.html)
* **Contenido:**
  * Buscador inteligente semántico y por palabras clave en tiempo real.
  * Pestañas de filtrado dinámico por categoría (*"Todos"*, *"Backend"*, *"Cloud OCI"*, *"Data Science"*, *"Frontend"*).
  * Catálogo de documentos recomendados con cálculo de porcentaje de similitud (`98%`, `95%`) y enlace directo a inferencia.

---

### 📊 4. `dashboard.html` (Módulo 4: Dashboard & Métricas KPIs)
* **Opción del Menú:** `Dashboard & Métricas` (`<a href="dashboard.html">`)
* **Página:** [`dashboard.html`](file:///Users/greccy/Documents/Alura%20Oracle/Hackaton/G9-LATAM-Team-28/ux-ui/dashboard.html)
* **Contenido:**
  * Tarjetas ejecutivas de KPIs (Documentos Procesados `1,248`, Precisión Promedio `96.4%`, Latencia `~42ms`, Categoría Dominante).
  * Indicadores de estado de infraestructura OCI Compute (VM.Standard2.4) y contenedor Docker (`techmind-api:latest`).
  * Ejecutor de suite de pruebas automatizadas Pytest / Jest (4/4 Passed).

---

### 📜 5. `historial.html` (Módulo 5: Historial de Consultas)
* **Opción del Menú:** `Historial de Consultas` (`<a href="historial.html">`)
* **Página:** [`historial.html`](file:///Users/greccy/Documents/Alura%20Oracle/Hackaton/G9-LATAM-Team-28/ux-ui/historial.html)
* **Contenido:**
  * Cuadrícula de registro histórico guardado en `localStorage`.
  * Acción de recarga de documentos pasados para re-evaluación en el clasificador.
  * Opción de limpieza completa del historial.

---

### 🔑 6. `login.html` (Módulo de Autenticación)
* **Opción del Menú:** `Acceso / Registro` (`<a href="login.html">`)
* **Página:** [`login.html`](file:///Users/greccy/Documents/Alura%20Oracle/Hackaton/G9-LATAM-Team-28/ux-ui/login.html)
* **Contenido:**
  * Pestañas para inicio de sesión y registro de cuentas corporativas.
  * Medidor de fuerza de contraseña y conmutadores de visibilidad.
