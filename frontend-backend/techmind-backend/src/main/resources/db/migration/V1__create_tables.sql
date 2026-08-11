-- =============================================================================
-- Migration Script: V1__create_tables.sql
-- Description: Create initial schema for users and contenidos tables
-- Application: TechMind Backend
-- =============================================================================

-- Table: users
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    rol VARCHAR(50) DEFAULT 'USER',
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: contenidos
CREATE TABLE IF NOT EXISTS contenidos (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255),
    texto TEXT,
    categoria VARCHAR(255),
    confianza VARCHAR(255),
    palabras_clave VARCHAR(255),
    user_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_contenidos_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Indexes for optimal performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_contenidos_user_id ON contenidos(user_id);
