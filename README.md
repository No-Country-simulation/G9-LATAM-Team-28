# Techmind-organizador-contenido-tecnico
MVP para organizar contenido técnico mediante Machine Learning, FastAPI y Oracle Cloud Infrastructure (OCI)..

# TechMind

Organizador Inteligente de Contenido Técnico

## Descripción

TechMind es un MVP desarrollado para organizar contenido técnico mediante técnicas de Ciencia de Datos, Machine Learning y una API REST construida con FastAPI.

## Tecnologías

- Python
- Pandas
- Scikit-Learn
- FastAPI
- Oracle Cloud Infrastructure (OCI)

## Estado del proyecto

🚧 En desarrollo

### Avances

- Arquitectura definida
- Dataset inicial
- Notebook base
- Documentación inicial

### Próximos pasos

- Entrenar el modelo
- Crear la API
- Integración con OCI
- Despliegue

## Equipo

- Greccy Verde
- Frank Carlos Santos Patiño
- Cesar Basilio
- Alison Liascos Diaz
- Bryan Vasquez
- Angel Arturo Vega de la Rosa
- Gabriel Chacón


## Inicio 

# TechMind - Organizador de Contenido Técnico

## Introducción

Este repositorio contiene la **base de datos inicial** del proyecto TechMind, un MVP orientado a organizar contenido técnico de forma inteligente mediante técnicas de Ciencia de Datos y Machine Learning.

La base de datos fue creada como punto de partida para estructurar y clasificar contenidos relacionados con distintas áreas tecnológicas, incluyendo Python, Backend, DevOps, Business Intelligence, Inteligencia Artificial, Frontend y Bases de Datos.

Esta información servirá como insumo para el desarrollo del modelo y la API del proyecto, permitiendo avanzar en la automatización de la organización del contenido técnico.

## Propósito de la base de datos

El objetivo de esta base inicial es establecer una estructura ordenada que facilite el análisis, clasificación y futura expansión del contenido del proyecto.

Además, esta base puede ser **modificada, ampliada o mejorada por los integrantes del equipo**, con la finalidad de enriquecer el proyecto, incorporar nuevos temas y adaptarse a futuras necesidades académicas o técnicas.

## Estructura general

La base de datos incluye campos como:

- `id`
- `titulo`
- `texto`
- `categoria`

## Nota

Esta base de datos corresponde a una versión inicial del proyecto y está pensada para evolucionar con el tiempo a medida que el equipo incorpore nuevos contenidos, categorías y mejoras en la calidad de los datos.


# 🚀 TechMind Backend

Backend del proyecto **TechMind – Organización Inteligente del Conocimiento Técnico**, desarrollado como parte del Hackathon Oracle Next Education (ONE) + Alura.

## 📖 Descripción

TechMind es una plataforma orientada a la organización inteligente del conocimiento técnico mediante el uso de Inteligencia Artificial. El backend proporciona los servicios necesarios para gestionar la lógica de negocio, la persistencia de datos y la comunicación con futuros modelos de Machine Learning.

Actualmente el proyecto se encuentra en fase de construcción de la arquitectura base utilizando Spring Boot.

---

# 🛠 Tecnologías utilizadas

- Java 21
- Spring Boot 4
- Spring Web
- Spring Data JPA
- Hibernate ORM
- Maven
- Apache Tomcat Embebido
- Git
- GitHub

---

# 📂 Arquitectura del proyecto

El proyecto sigue una arquitectura por capas para facilitar el mantenimiento y escalabilidad.

```
src
└── main
    ├── java
    │   └── com.techmind
    │       ├── controller
    │       ├── service
    │       ├── repository
    │       ├── entity
    │       ├── dto
    │       ├── config
    │       └── TechmindBackendApplication.java
    │
    └── resources
        ├── application.properties
        └── static
```

---

# ✅ Avances realizados

## Configuración del proyecto

- Creación del proyecto con Spring Boot.
- Configuración mediante Maven.
- Configuración para Java 21.
- Integración de Spring Web.
- Integración de Spring Data JPA.
- Configuración inicial de Hibernate.

---

## Servidor

Se configuró correctamente el servidor embebido de Spring Boot (Apache Tomcat).

La aplicación inicia correctamente mediante:

```
mvn spring-boot:run
```

o directamente desde IntelliJ IDEA.

---

## Persistencia

Se inició la configuración de la capa de persistencia mediante:

- Spring Data JPA
- Hibernate

Actualmente se encuentra en proceso de integración con la base de datos.

Durante el desarrollo se identificó y solucionó parte de la configuración relacionada con:

- DataSource
- Driver JDBC
- Variables de entorno
- Configuración del application.properties

---

## Arquitectura

Se comenzó la separación lógica del proyecto en diferentes capas:

- Controllers
- Services
- Repositories
- Entities
- DTOs
- Configuración

Esta estructura permitirá mantener una arquitectura limpia y desacoplada.

---

## Gestión del proyecto

Se configuró el proyecto para utilizar:

- Git
- GitHub
- Maven

permitiendo el trabajo colaborativo y el control de versiones.

---

# 🔄 Estado actual

Actualmente el backend cuenta con:

- ✔ Proyecto Spring Boot funcional
- ✔ Configuración Maven
- ✔ Configuración Java 21
- ✔ Servidor Tomcat funcionando
- ✔ Integración con Spring Data JPA
- ✔ Configuración inicial de Hibernate
- ✔ Arquitectura base del proyecto
- ✔ Preparación para integración con base de datos

---

# 🚧 En desarrollo

Las siguientes funcionalidades forman parte del siguiente sprint de desarrollo:

- Implementación de entidades.
- Creación de repositorios JPA.
- Desarrollo de servicios.
- Creación de controladores REST.
- Implementación de DTOs.
- Validaciones mediante Bean Validation.
- Manejo global de excepciones.
- Documentación con Swagger/OpenAPI.
- Integración con PostgreSQL.
- Variables de entorno para despliegue.
- Integración con Oracle Cloud Infrastructure (OCI).
- Autenticación y autorización con Spring Security.
- Consumo de modelos de Inteligencia Artificial.
- Integración con el frontend.

---

# 📌 Objetivo

Construir una API REST robusta que permita administrar el conocimiento técnico de manera inteligente, integrando modelos de Inteligencia Artificial para facilitar la organización, consulta y recomendación de información.

---

# 👨‍💻 Equipo

Proyecto desarrollado durante el Hackathon Oracle Next Education (ONE) + Alura.

Backend Developer:
- Desarrollo de la arquitectura del backend.
- Configuración de Spring Boot.
- Integración de JPA/Hibernate.
- Configuración del servidor.
- Preparación para despliegue en Oracle Cloud.

---

# 📈 Próximos pasos

1. Configurar PostgreSQL.
2. Crear el modelo de datos.
3. Implementar la capa Repository.
4. Desarrollar la lógica de negocio.
5. Exponer los endpoints REST.
6. Integrar autenticación JWT.
7. Consumir el modelo de IA.
8. Desplegar la API en Oracle Cloud.

---

## Estado del proyecto

**Versión actual:** `0.1.0-SNAPSHOT`

**Estado:** 🟡 En desarrollo

---

# 🚀 TechMind Frontend & UX/UI

Frontend del proyecto TechMind – Organización Inteligente del Conocimiento Técnico, desarrollado como parte del Hackathon Oracle Next Education (ONE) + Alura.

# 📖 Descripción

El Frontend de TechMind constituye la interfaz visual e interactiva que permite a los usuarios registrar, visualizar, buscar y consultar contenidos técnicos de manera organizada y eficiente. Diseñado bajo metodologías centradas en el usuario (UX/UI), proporciona un flujo ágil para procesar textos técnicos y consumir en tiempo real los resultados de clasificación y análisis generados por los modelos de Inteligencia Artificial (desarrollados en Python) y expuestos a través de la API REST del backend.

# 🛠 Tecnologías y Herramientas utilizadas

- HTML5
- CSS3 (Diseño responsivo y personalizado)
- JavaScript ES6+ (Lógica de interacción en el cliente y peticiones HTTP vía Fetch API)
- Python (Desarrollo, procesamiento de texto y entrenamiento de modelos de Ciencia de Datos / IA)
- Canva (Diseño visual, prototipado)
- Visual Studio Code
- Git / GitHub

# 📂 Arquitectura del proyecto

El cliente sigue una estructura modular para garantizar la separación de responsabilidades entre vistas, estilos y lógica de comunicación.

src
└── main
    └── resources
        ├── static
        │   ├── css
        │   │   ├── styles.css
        │   │   └── components.css
        │   ├── js
        │   │   ├── app.js       (Lógica principal de la aplicación)
        │   │   ├── api.js       (Peticiones a la API REST)
        │   │   └── ui.js        (Renderizado dinámico de componentes en pantalla)
        │   ├── assets
        │   │   └── images
        │   └── index.html

# ✅ Avances realizados

## Diseño UX/UI y Prototipado
- Investigación de usuarios e identificación de necesidades de lectura y catalogación de contenidos técnicos.
- Diseño de la interfaz, estructuración de pantallas y definición de componentes visuales en Canva.
- Definición de la guía de estilos (paleta de colores, tipografía e iconografía) enfocada en la legibilidad y la usabilidad.
- Aplicación de buenas prácticas de UX Writing para optimizar la interacción y guiar al usuario en la carga e interpretación de los resultados del modelo de datos.

## Estructura Base del Frontend
- Maquetación HTML5 semántica de las vistas principales:
  - Formulario para ingreso de contenido técnico (título y texto).
  - Panel visual de resultados procesados (categorías, palabras clave, porcentaje de probabilidad).
  - Buscador y filtro por temas.
- Estilizado visual con CSS3 adaptativo (Responsive Design) para escritorio y dispositivos móviles.

## Estructura Lógica y Funcional de Scripts (JavaScript)
- Configuración de controladores en JavaScript (`app.js`, `ui.js`) para la manipulación dinámica del DOM y la gestión de eventos de usuario.
- Definición del módulo de comunicación (`api.js`) preparado para realizar peticiones asíncronas HTTP (`fetch`) enviando solicitudes `POST /contenido` hacia la API REST.

## Gestión del proyecto
- Control de versiones e integración en el repositorio oficial de GitHub mediante Git.

# 🔄 Estado actual

Actualmente el frontend cuenta con:
✔ Prototipo y maquetación visual definidos en Canva
✔ Interfaz HTML5/CSS3 funcional y responsiva
✔ Estructura de código JavaScript modular para consumo de APIs
✔ Formulario de carga y área de visualización dinámica de respuestas JSON
✔ Preparación del flujo interactivo para conexión con la API REST y el modelo de IA en Python

# 🚧 En desarrollo

Las siguientes funcionalidades forman parte del siguiente sprint de desarrollo:
- Integración completa con los endpoints del Backend en Spring Boot (peticiones POST /contenido).
- Manejo de estados de carga (spinners/loaders) durante el procesamiento del modelo de IA en Python.
- Control de errores de red y validación visual de formularios de entrada.
- Implementación de vista para búsqueda semántica y filtrado dinámico por categorías.
- Ajustes de accesibilidad (WCAG) y refinamiento continuo del UX Writing.
- Despliegue de archivos estáticos en infraestructura de Oracle Cloud Infrastructure (OCI).

# 📌 Objetivo

Proveer una experiencia de usuario intuitiva, accesible y fluida que permita transformar textos y notas técnicas dispersas en un repositorio de conocimiento estructurado y fácilmente reutilizable.

# 👨‍💻 Equipo

Proyecto desarrollado durante el Hackathon Oracle Next Education (ONE) + Alura.
UX/UI Designer & UX Writer:
- Diseño de la experiencia de usuario (UX), redacción de contenidos (UX Writing) y diseño de interfaz (UI) en Canva.
- Maquetación y estructura del cliente web (HTML, CSS, JavaScript).
- Alineación del flujo visual con la estructura de respuestas JSON entregadas por el backend y el modelo de IA.

# 📈 Próximos pasos

- Conectar peticiones Fetch en JavaScript con los endpoints expuestos por la API REST.
- Renderizar en tiempo real la clasificación y extracción de palabras clave retornadas por la IA.
- Diseñar e implementar el dashboard de visualización y consulta de métricas/categorías.
- Realizar pruebas de usabilidad con la interfaz terminada.
- Desplegar el cliente en OCI.

# Estado del proyecto

Versión actual: 0.1.0-SNAPSHOT
Estado: 🟡 En desarrollo
