# Sistema de Empleados y Asistencias (Spring Boot + PostgreSQL)

Proyecto para **Desarrollo de Software I - Universidad Israel** basado en la arquitectura clean code.

## Arquitectura

```
Navegador (http://localhost:8081)
        |
        v
+---------------------+     WebClient      +---------------------+     JPA/Hibernate     +----------------+
|   empleadosweb      | -----------------> |    empleadosapi     | --------------------> |  PostgreSQL    |
|   MVC + Thymeleaf   |  http://localhost  |   API REST /api     |                       |  empleadosdb   |
|   Puerto 8081       |     :8080/api      |   Puerto 8080       |                       |  Puerto 5432   |
+---------------------+                    +---------------------+                       +----------------+
```

Dos proyectos Maven independientes (arquitectura limpia por capas):

| Proyecto | Descripción | Capas |
|---|---|---|
| **empleadosapi** | API REST con Spring Boot 4.1, JPA, PostgreSQL | entities, repository (JPQL), model/dto, services, controller, exception |
| **empleadosweb** | Cliente web MVC con Thymeleaf + layout dialect + plantilla Modernize (Bootstrap 5) | model/dto, services (WebClient), controller, configuration, templates |

## Funcionalidad

- **CRUD completo de empleados** (ficha principal = código del empleado, con código alterno, cédula, datos personales, puesto, departamento y horario — campos según las pantallas de referencia).
- **Módulo de asistencias** atado al código del empleado: marcación de ENTRADA y SALIDA, cálculo automático PUNTUAL/ATRASO según el horario 08:30–17:00 con 10 min de tolerancia, una sola marcación por día.
- **Catálogos**: Horarios (08:30–17:00 precargado), Áreas (Trefilados, Administración, Calidad, Logística, Laminados, Fundición) y Departamentos (Transformación Digital, Costos, Inteligencia del Negocio, Ventas, etc.) con CRUD propio.
- **Relaciones JPA**: `Area 1—N Departamento`, `Departamento 1—N Empleado`, `Horario 1—N Empleado`, `Empleado 1—N Asistencia`.
- **Consultas JPQL** con `@Query`: JOIN FETCH, LIKE, BETWEEN, COUNT (ver repositorios).

## Requisitos

- Java 21 (JDK)
- Maven (o Eclipse/STS/IntelliJ con soporte Maven)
- PostgreSQL 14+ corriendo en `localhost:5432`

## Paso a paso para ejecutar

### 1. Base de datos
```sql
-- En pgAdmin o psql como usuario postgres:
CREATE DATABASE empleadosdb;
```
(o ejecutar `sql/01_crear_base_datos.sql`). Las tablas y datos semilla se crean solas al arrancar el API.

