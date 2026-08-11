-- =============================================================================
-- Migration Script: V2__seed_initial_data.sql
-- Description: Seed initial test/admin user and sample contents
-- Application: TechMind Backend
-- =============================================================================

-- Insert initial admin / test user if not exists
-- PasswordBCrypt for 'password123': $2a$10$e8W/X22rD2.0nS0V0dG0v.xW5mH1O5gE7A7pA.2/6Q
INSERT INTO users (email, password, nombre, rol, creado_en)
VALUES (
    'admin@techmind.com',
    '$2a$10$e8W/X22rD2.0nS0V0dG0v.xW5mH1O5gE7A7pA.2/6Q',
    'TechMind Admin',
    'ADMIN',
    NOW()
) ON CONFLICT (email) DO NOTHING;

-- Insert sample content for admin user
INSERT INTO contenidos (titulo, texto, categoria, confianza, palabras_clave, user_id, created_at)
SELECT 
    'Bienvenido a TechMind',
    'Este es un contenido de ejemplo inicial cargado automáticamente por la migración Flyway.',
    'Tecnología',
    'Alta',
    'techmind, inicio, bienvenida',
    id,
    NOW()
FROM users 
WHERE email = 'admin@techmind.com'
AND NOT EXISTS (
    SELECT 1 FROM contenidos WHERE titulo = 'Bienvenido a TechMind'
);
