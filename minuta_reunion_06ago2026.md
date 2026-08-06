# Minuta de Reunion: Incorporacion de Grecy Verde al Equipo

**Proyecto:** Hackathon ONE – Proyectos G9 | Alura + Oracle  
**Fecha:** 06 de agosto de 2026  
**Hora:** 10:00 AM CEST  
**Fase:** Tercera semana de desarrollo  
**Modalidad:** Reunion de equipo  
**Elaborada por:** Cesar Basilio (Data Analyst)  

---

## 1. Objetivo de la reunion

Revisar el avance general del proyecto, presentar los componentes desarrollados por cada miembro, integrar el nuevo modelo de clasificacion y establecer responsabilidades y fechas para la entrega de la solucion integrada.

---

## 2. Participantes

| Nombre | Rol |
|--------|-----|
| Grecy Verde | Project Manager / Back-End Developer |
| Cesar Basilio | Data Analyst |
| Gabriel Chacon | UX/UI Designer |
| Angel Vega | Back-End Developer |

**Ausente:** Francisco (abandono el proyecto hace ~2 semanas; su base de datos no fue entregada y no se utilizo).

---

## 3. Incorporacion de Grecy Verde

- **Rol:** Project Manager (se reconocio que el rol de PM es limitado en esta etapa; se le sugirio enfocarse en sus habilidades tecnicas de backend/desarrollo web).
- **Ubicacion:** Actualmente en Espana (originaria de Venezuela).
- **Formacion:** Cursado Tech Advance (parte de Backend); especialidad en desarrollo web y backend.
- **Contexto:** Reviso el historico de mensajes y la plataforma de No Country para ponerse al dia.
- **Disponibilidad:** Debe actualizar su disponibilidad en No Country (actual: 8 AM-12 PM → real: 6 PM en adelante, hora Espana).

---

## 4. Estado general del proyecto

El equipo estima que el proyecto presenta un avance aproximado del **85 %**.

### Componentes desarrollados

- Modelo de datos.
- Dataset de contenidos tecnicos (8.000 registros, 45 columnas, en ingles).
- Modelo de Ciencia de Datos entrenado (82.55% de exactitud).
- Interfaz preliminar (HTML + CSS).
- Codigo desarrollado en Java (Spring Boot v3.4).
- Backend containerizado en Docker con PostgreSQL.
- Generacion del archivo `index.html` desde el proyecto Java.

### Componentes pendientes

- Integracion de todos los archivos y componentes.
- Puesta en funcionamiento local de la solucion completa.
- Registro de usuarios.
- Inicio de sesion.
- Configuracion de los servicios necesarios en OCI.
- Despliegue de la solucion.
- Pruebas funcionales integrales.
- Documentacion final.
- Preparacion de la demostracion y grabacion del video.

---

## 5. Modelo de IA y Analisis de Datos (Cesar Basilio)

### Proyecto
Clasificacion de cursos online por categorias a partir del titulo y descripcion.

### Base de Datos
- **Original:** Demasiado pequena; se sustituyo por dataset de Kaggle.
- **Nueva:** 8.000 registros, 45 columnas, en ingles.
- **Requisito:** El modelo requiere input en ingles para las consultas.

### Evolucion del Modelo
| Modelo | Exactitud |
|--------|-----------|
| Modelo inicial | 64.12% |
| Regresion logistica | 74.8% |
| Modelo optimizado (validacion cruzada en 5 bloques) | **82.55%** (confianza cercana al 100%) |

### Decision del Equipo
- **Problema detectado:** El modelo anterior de Gabriel carecia de comprobacion/evaluacion adecuada.
- **Decision unanime:** Usar el nuevo modelo de Cesar como base definitiva del proyecto.

---

## 6. Interfaz y Arquitectura Tecnica

### FastAPI (Cesar)
- Interfaz funcional que acepta: titulo, texto libre y archivos PDF/TXT.
- La clasificacion mejora combinando titulo + texto (asi esta entrenado el modelo).

### Frontend (Gabriel)
- `index.html` + CSS ya desarrollados.
- **Pendiente:** Integrar login/registro de Angel y agregar el campo de titulo a la interfaz existente.

### Backend (Angel)
- **Tecnologia:** Spring Boot v3.4, PostgreSQL containerizado en Docker.
- **Variables de entorno necesarias:**
  - `DATABASE_URL`
  - `DATABASE_USERNAME`
  - `DATABASE_PASSWORD`
- **Nota:** Las variables son locales y no se comparan entre usuarios.
- **Instrucciones de Docker:** Se compartiran para correr el proyecto sin instalar Python ni IDE.

### OCI (Oracle Cloud Infrastructure)
- **Requisito:** Vincular al menos 1 servicio (se pueden vincular hasta 7).
- **Cuota actual:** 150 secretos (antes 20), 30 GB de almacenamiento.
- **Tamano del proyecto en Docker:** ~118 MB.

---

## 7. Acuerdos y compromisos

### Cesar Basilio — Data Analyst

Cesar se encargara de subir a la rama principal del repositorio de GitHub:

- Modelo entrenado (.pkl o .joblib).
- Archivo `main.py` (FastAPI).
- Analisis completo del modelo.
- Base de datos original.
- Base de datos limpia.
- Imagen de referencia de diseno.

**Fecha limite:** 06 de agosto de 2026, antes de las 13:00.  
**Accion adicional:** Retirar los archivos anteriores y notificar al grupo en el canal general.

### Gabriel Chacon — UX/UI Designer

Gabriel trabajara en:

- Descargar los archivos de Cesar.
- Integrar el nuevo modelo en la interfaz.
- Agregar el campo de titulo a la interfaz existente.
- Adaptar visualmente los componentes desarrollados.
- Subir sus cambios al repositorio.

**Fecha limite:** 07 de agosto de 2026, por la manana.  
**Notificacion:** Avisar a Angel al terminar.

### Angel Vega — Back-End Developer

Angel se encargara de:

- Unificar los archivos y componentes desarrollados por el equipo.
- Integrar el modelo, la interfaz y el codigo Back-End.
- Ejecutar la solucion completa en un entorno local.
- Identificar y corregir posibles errores de integracion en colaboracion con Grecy.
- Coordinar con Grecy la configuracion y subida de la solucion a OCI.
- Subir todo el backend al GitHub (credenciales limpias, HTML correcto, instrucciones de Docker incluidas).

**Fecha limite:** Antes del 10 de agosto de 2026.  
**Resultado esperado:** Solucion integrada y operativa localmente antes del despliegue.

### Grecy Verde — Project Manager

Grecy se encargara de:

- Apoyar a Angel con la vinculacion de servicios OCI (Angel enviara las variables de entorno por privado).
- Subir minuta de reunion y documentacion de PM al repositorio.
- Actualizar su disponibilidad en la plataforma No Country (6 PM en adelante, hora Espana).
- Realizar el seguimiento de los compromisos.
- Mantener actualizado el backlog y el cronograma.
- Coordinar y colaborar en las pruebas funcionales.
- Colaborar en la organizacion y preparacion de la demostracion de la solucion.
- Colaborar en la planificacion y grabacion del video.
- Gestionar las comunicaciones sobre bloqueos, avances y fechas limite.

**Disponibilidad:** Desde las 6:00 PM (hora Espana) o fines de semana.

---

## 8. Fecha objetivo

Se establece como fecha objetivo disponer de la solucion integrada y funcional el:

**Lunes 10 de agosto de 2026, 10:00 AM.**

En esa reunion se revisaran los avances integrados y se aclararan dudas.

### Semana siguiente (11-15 de agosto)

La solucion debe estar disponible para comenzar durante la siguiente semana:

- Las pruebas finales.
- La preparacion del guion.
- La grabacion de la demostracion.
- La edicion del video.
- La revision de la documentacion.
- La preparacion de la entrega final.

### Demo Day

- **Fecha:** 25 o 27 de agosto de 2026
- **Horario:** Sesion nocturna (~2:00-3:00 AM hora Espana)

---

## 9. Riesgos identificados

- Posibles conflictos durante la integracion de archivos y ramas.
- Tiempo limitado para desarrollar registro e inicio de sesion.
- Problemas de acceso, permisos o configuracion en OCI.
- Falta de tiempo para realizar pruebas antes de la grabacion.
- Documentacion incompleta al finalizar el desarrollo.
- Cambios de ultima hora que afecten la estabilidad de la solucion.

---

## 10. Medidas de seguimiento

- Comunicar inmediatamente cualquier bloqueo tecnico.
- Evitar incorporar funcionalidades no esenciales antes de completar la integracion.
- Priorizar una version minima, estable y demostrable.
- Verificar cada integracion en entorno local antes de subirla a OCI.
- Mantener el repositorio actualizado y documentar los cambios.
- Preparar progresivamente el README y no dejarlo para el final.
- Definir y probar al menos tres ejemplos de entrada y salida.
- Congelar cambios funcionales una vez que la solucion este lista para grabacion.

---

## 11. Resumen de tareas

| Tarea | Responsable | Fecha limite | Estado |
|-------|-------------|--------------|--------|
| Subir 6 archivos al GitHub (modelo, main, analisis, 2 BD, imagen) | Cesar Basilio | 06/08/2026 – 13:00 | En proceso |
| Actualizar interfaz con nuevo modelo y campo de titulo | Gabriel Chacon | 07/08/2026 – manana | Pendiente |
| Integrar todos los archivos y componentes | Angel Vega | 10/08/2026 | Pendiente |
| Ejecutar la solucion localmente | Angel Vega | 10/08/2026 | Pendiente |
| Configurar servicios de OCI | Angel Vega y Grecy Verde | 10/08/2026 | Pendiente |
| Subir minuta y documentacion de PM al repositorio | Grecy Verde | 06/08/2026 | Pendiente |
| Actualizar disponibilidad en plataforma No Country | Grecy Verde | 06/08/2026 | Pendiente |
| Completar registro de usuarios | Equipo de desarrollo | Antes de la grabacion | Pendiente |
| Completar inicio de sesion | Equipo de desarrollo | Antes de la grabacion | Pendiente |
| Realizar pruebas funcionales | Todo el equipo | Despues de la integracion | Pendiente |
| Preparar demostracion y video | Todo el equipo | Semana siguiente | Pendiente |

---

## 12. Proximas reuniones

| Fecha | Hora | Objetivo |
|-------|------|----------|
| Lunes 10 de agosto de 2026 | 10:00 AM | Revision de avances integrados y aclaracion de dudas |
| Semanas 5 y 6 (11-22 de agosto) | Por definir | Dedicadas al video y preparacion para el Demo Day |
| Demo Day | 25 o 27 de agosto de 2026 | Presentacion final del proyecto |

---

**Documentacion elaborada por:** Cesar Basilio  
**Fecha de elaboracion:** 06 de agosto de 2026  
**Hora de la reunion:** 10:00 AM CEST