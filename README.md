# Sistema de Empleados y Asistencias (Spring Boot + PostgreSQL)

Proyecto para **Desarrollo de Software I - Universidad Israel** basado en la arquitectura del PDF S5 (Cliente Web + API) y el proyecto de referencia `consumoweb`.

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

### 2. Credenciales
Editar `empleadosapi/src/main/resources/application.properties` si su usuario/clave de PostgreSQL no es `postgres/postgres`:
```properties
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 3. Importar en Eclipse/STS
`File > Import > Maven > Existing Maven Projects` y seleccionar la carpeta `sistema-empleados` (importa ambos proyectos).

### 4. Arrancar (en este orden)
1. **empleadosapi** → Run As > Spring Boot App → verificar en `http://localhost:8080/api/areas`
2. **empleadosweb** → Run As > Spring Boot App → abrir `http://localhost:8081`

### 5. Probar el flujo
1. Revisar catálogos (Áreas, Departamentos, Horarios ya vienen precargados).
2. Crear un empleado en **Talento Humano > Empleados > Nuevo Empleado**.
3. Ir a **Marcar Asistencia**, elegir el empleado y marcar ENTRADA (antes de 08:40 = PUNTUAL, después = ATRASO).
4. Marcar SALIDA al final del día.
5. Ver el historial en **Asistencias** (filtrable por código de empleado).

## Endpoints principales del API

| Método | Ruta | Descripción |
|---|---|---|
| GET/POST | `/api/empleados` | Listar (con `?buscar=`) / crear |
| GET/PUT/DELETE | `/api/empleados/{codigo}` | Consultar / actualizar / eliminar |
| POST | `/api/asistencias/entrada` | Marcar entrada `{idEmpleado, observacion}` |
| PUT | `/api/asistencias/salida` | Marcar salida |
| GET | `/api/asistencias/empleado/{codigo}` | Asistencias de un empleado |
| GET/POST | `/api/areas`, `/api/departamentos`, `/api/horarios` | Catálogos |
