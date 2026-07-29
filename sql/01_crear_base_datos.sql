-- ============================================================
-- Sistema de Empleados y Asistencias - Universidad Israel
-- Base de datos PostgreSQL
-- Ejecutar conectado como usuario postgres
-- ============================================================

-- 1. Crear la base de datos
CREATE DATABASE empleadosdb
    WITH ENCODING 'UTF8'
    TEMPLATE template0;

-- 2. (Opcional) Crear un usuario dedicado
-- CREATE USER empleados_user WITH PASSWORD 'empleados123';
-- GRANT ALL PRIVILEGES ON DATABASE empleadosdb TO empleados_user;

-- NOTA:
-- Las tablas (area, departamento, horario, empleado, asistencia)
-- se crean AUTOMATICAMENTE al arrancar el proyecto empleadosapi,
-- gracias a spring.jpa.hibernate.ddl-auto=update.
-- Los datos iniciales (horario 08:30-17:00, areas y departamentos)
-- se insertan automaticamente desde src/main/resources/data.sql.
