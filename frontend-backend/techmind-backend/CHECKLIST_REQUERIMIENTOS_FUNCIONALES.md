# Checklist de Requerimientos Funcionales - TechMind

**Proyecto:** TechMind - Organizador Inteligente de Conocimiento Técnico  
**Hackathon:** Oracle Next Education (ONE) + Alura (G9-LATAM-Team-28)  
**Fecha de Evaluación / Actualización:** 13 de Agosto, 2026  
**Ubicación del Backend / Frontend:** `techmind-backend` (Spring Boot 4 + Java 21 + HTML5/CSS3/JS ES6)  
**Ubicación del Servicio ML:** `model-ml` (FastAPI + Python + Scikit-Learn NLP)

---

## 📊 Resumen Ejecutivo del Estado del Proyecto

| Categoría / Módulo | Funcionalidades Totales | Implementadas (✅) | Parciales / Simuladas (⚠️) | Pendientes (❌) | Porcentaje de Avance |
| :--- | :---: | :---: | :---: | :---: | :---: |
| **1. Clasificación e Inferencia ML** | 6 | 6 | 0 | 0 | **100%** |
| **2. Explicabilidad & API REST** | 4 | 3 | 1 | 0 | **87.5%** |
| **3. Búsqueda & Recomendaciones** | 4 | 4 | 0 | 0 | **100%** |
| **4. Dashboard & Métricas** | 4 | 3 | 1 | 0 | **87.5%** |
| **5. Historial & Persistencia** | 4 | 4 | 0 | 0 | **100%** |
| **6. Autenticación & Usuarios** | 4 | 3 | 1 | 0 | **87.5%** |
| **7. Backend REST API & DB** | 5 | 5 | 0 | 0 | **100%** |
| **8. Interfaz & UX/UI** | 5 | 5 | 0 | 0 | **100%** |
| **TOTALES GENERALES** | **36** | **33** | **3** | **0** | **~94%** |

---

## 📋 Checklist Completo en Formato Tabla: Requerimientos Funcionales

| ID | Módulo | Requerimiento Funcional | Capa | Estado | Observación / Detalle Técnico |
| :--- | :--- | :--- | :---: | :---: | :--- |
| **RF-01** | Inferencia | Recepción de título y texto técnico desde formulario web | Frontend / API | ✅ **Implementado** | Captura datos en `index.html` y valida que al menos un campo o archivo esté presente. |
| **RF-02** | Inferencia | Clasificación automática de categorías técnicas mediante NLP | ML / Backend | ✅ **Implementado** | Modelo TF-IDF + LogisticRegression expuesto en FastAPI (`POST /classify`) y consumido por Spring Boot (`POST /contenido`). |
| **RF-03** | Inferencia | Carga de archivos adjuntos por lotes (`.txt`, `.csv`, `.pdf`, `.docx`, imágenes) | Frontend / ML | ✅ **Implementado** | `document_service.py` procesa PDF, DOCX, TXT e imágenes (OCR Tesseract). `dropZone` permite arrastrar archivos. |
| **RF-04** | Inferencia | Extracción de palabras clave y cálculo del porcentaje de confianza (%) | ML / Backend | ✅ **Implementado** | El pipeline retorna `confianza` (ej: `96%`) y `palabrasClave` en la respuesta JSON. |
| **RF-05** | Inferencia | Botones de 1-clic con ejemplos de prueba predefinidos | Frontend | ✅ **Implementado** | Botones de Spring Boot, OCI Cloud y Python ML precargan el formulario instantáneamente. |
| **RF-06** | Inferencia | Mecanismo de contingencia (fallback local JS) ante desconexión | Frontend | ✅ **Implementado** | Si el servidor ML o Backend no responde, `clasificarContenidoLocal()` entrega estimación heurística. |
| **RF-07** | Explicabilidad | Visor modal de respuesta REST API HTTP 200 OK con copiado | Frontend | ✅ **Implementado** | Modal interactivo en `explicabilidad.html` muestra el JSON formateado con botón de copiar. |
| **RF-08** | Explicabilidad | Indicador visual de salud y estado del microservicio ML (`/health`) | Backend / ML | ✅ **Implementado** | `statusBadge` consulta `GET /contenido/ml-health` para mostrar la versión y estado online. |
| **RF-09** | Explicabilidad | Importancia de Atributos (Feature Importance Weights) real por token | ML / Frontend | ⚠️ **Parcial** | Ponderación en UI (`+0.48`, `+0.42`), generada con tokens relevantes de la respuesta. |
| **RF-10** | Explicabilidad | Documentación de contrato de endpoints HTTP Request / Response | Backend | ✅ **Implementado** | Documentado con OpenAPI / Swagger 3 en Spring Boot (`/swagger-ui.html`). |
| **RF-11** | Búsqueda | Buscador en tiempo real por palabras clave en catálogo | Frontend / API | ✅ **Implementado** | Consume `GET /contenido/buscar?query=...` consultando la BD PostgreSQL en tiempo real con debounce. |
| **RF-12** | Búsqueda | Filtrado interactivo por categorías (*Todos*, *Backend*, *Cloud OCI*, etc.) | Frontend / API | ✅ **Implementado** | Pestañas de filtrado dinámico operacionales que consultan la base de datos por categoría. |
| **RF-13** | Búsqueda | Consulta y persistencia de recomendaciones en tiempo real desde la BD | Backend / BD | ✅ **Implementado** | `recomendaciones.html` consulta `GET /contenido/historial` y `GET /contenido/buscar` destacando ítems BD PostgreSQL. |
| **RF-14** | Búsqueda | Re-dirección directa desde recomendación hacia el clasificador | Frontend | ✅ **Implementado** | Botón "Abrir" transfiere el título y resumen directamente a `index.html` para re-clasificación. |
| **RF-15** | Dashboard | Indicadores de infraestructura OCI Compute y contenedor Docker | Frontend / Cloud | ✅ **Implementado** | Badges de estado OCI VM.Standard2.4 y `techmind-api:latest` visibles en el panel. |
| **RF-16** | Dashboard | Visualización de KPIs ejecutivos (Documentos, Precisión, Latencia) | Frontend / BD | ✅ **Implementado** | Consume `GET /api/dashboard/metrics` calculando total de registros de BD PostgreSQL en tiempo real. |
| **RF-17** | Dashboard | Categoría dominante calculada automáticamente mediante SQL | Backend / BD | ✅ **Implementado** | `ContenidoRepository.findTopCategoria()` ejecuta consulta JPQL `GROUP BY categoria ORDER BY COUNT(*) DESC`. |
| **RF-18** | Dashboard | Ejecutor de suite de pruebas automatizadas Pytest / Jest | Frontend | ⚠️ **Parcial** | Simulación modal interactiva de consola de pruebas (4/4 passed). |
| **RF-19** | Historial | Registro y almacenamiento de consultas en `localStorage` | Frontend | ✅ **Implementado** | Inferencia guardada localmente y visualizada en tarjetas en `historial.html`. |
| **RF-20** | Historial | Recarga de documentos pasados para re-evaluación en inferencia | Frontend | ✅ **Implementado** | Opción "Cargar" en el historial lleva los datos al clasificador principal. |
| **RF-21** | Historial | Acción de limpieza completa del historial de usuario | Frontend / API | ✅ **Implementado** | Botón "Limpiar Registro" ejecuta `DELETE /contenido/historial` en la BD y en `localStorage`. |
| **RF-22** | Historial | Historial de consultas persistido en PostgreSQL por usuario | Backend / BD | ✅ **Implementado** | `GET /contenido/historial` recupera los registros guardados en PostgreSQL asociados al `userId` del JWT. |
| **RF-23** | Autenticación | Formulario web de Login e Inicio de Sesión con validaciones | Frontend | ✅ **Implementado** | Vista `login.html` con pestañas, selector de contraseña y validación. |
| **RF-24** | Autenticación | Formulario web de Registro de Usuarios Corporativos | Frontend | ✅ **Implementado** | Formulario de registro con medidor visual de fuerza de contraseña. |
| **RF-25** | Autenticación | Endpoints REST API de Registro y Login (`/api/auth/*`) | Backend | ✅ **Implementado** | `AuthController.java` expone `/register`, `/login` y `/me` con Spring Security + BCrypt + JWT. |
| **RF-26** | Autenticación | Conexión e integración del Login del Frontend con el Token JWT | Frontend / API | ✅ **Implementado** | `login.html` consume `POST /api/auth/login` y guarda el token JWT en `localStorage.setItem('techmind_token')`. |
| **RF-27** | Autenticación | Protección de endpoints restringidos con JWT Bearer Filter | Backend | ⚠️ **Parcial** | `SecurityConfig.java` implementa `JwtAuthenticationFilter` e inyecta usuario en SecurityContextHolder. |
| **RF-28** | Persistencia | Estructura de BD relacional con Flyway (`users` y `contenidos`) | Backend / BD | ✅ **Implementado** | Migraciones `V1__create_tables.sql` y `V2__seed_initial_data.sql` ejecutadas con éxito. |
| **RF-29** | Persistencia | Repositorios Spring Data JPA para entidades `User` y `Contenido` | Backend | ✅ **Implementado** | `UserRepository.java` y `ContenidoRepository.java` listos y configurados. |
| **RF-30** | Persistencia | Guardado automático de solicitudes clasificadas en PostgreSQL | Backend | ✅ **Implementado** | `MlService.java` guarda título, texto, categoría, confianza y palabras clave en la tabla `contenidos`. |
| **RF-31** | Servicio ML | Carga limpia del modelo `.pkl` en evento `lifespan` de FastAPI | ML | ✅ **Implementado** | Carga asíncrona del pipeline optimizado sin bloquear el hilo principal. |
| **RF-32** | Servicio ML | Inferencia no bloqueante ejecutada en Thread Pool | ML | ✅ **Implementado** | Inferencia pesada envuelta en `run_in_threadpool()` de Starlette. |
| **RF-33** | Interfaz UI | Diseño 100% responsivo (Móvil, Tablet y Escritorio) | Frontend | ✅ **Implementado** | Maquetado con Tailwind CSS v4, sidebar deslizable y cabecera responsive. |
| **RF-34** | Interfaz UI | Modo Claro / Oscuro (Theme Toggle) con persistencia | Frontend | ✅ **Implementado** | Selector de tema funcional en todas las páginas guardado en `localStorage`. |
| **RF-35** | Interfaz UI | Notificaciones flotantes en tiempo real (Sistema de Toasts) | Frontend | ✅ **Implementado** | Mensajes contextuales de Éxito, Advertencia, Error e Info. |
| **RF-36** | Interfaz UI | Accesibilidad por teclado y estándares ARIA (`role`, `tabindex`) | Frontend | ✅ **Implementado** | Compatible con teclas `Enter` / `Espacio` y lectores de pantalla. |

---

## 🟢 Estado de Implementaciones Recientes en `recomendaciones.html`

1. **Persistencia Real desde PostgreSQL:**
   - La pantalla realiza peticiones asíncronas HTTP `GET /contenido/historial` y `GET /contenido/buscar` al backend de Spring Boot.
   - Cada documento guardado en la base de datos se identifica claramente con un distintivo neón `BD PostgreSQL`.

2. **Búsqueda Dinámica en Tiempo Real:**
   - Al escribir en la barra de búsqueda `searchInput`, se ejecuta una consulta con debounce de 300 ms al endpoint `GET /contenido/buscar?query=...`.
   - La búsqueda evalúa coincidencias en título, resumen, categoría y palabras clave guardadas en la base de datos.

3. **Interacción y Re-clasificación Instantánea:**
   - Al hacer clic en el botón **"Abrir"** de cualquier tarjeta, los datos del documento se transfieren automáticamente a la vista principal `index.html`, cargando su contenido en el formulario de inferencia.
