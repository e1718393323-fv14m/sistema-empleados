-- Consultas de verificacion (ejecutar sobre empleadosdb)

-- Catalogos precargados
SELECT * FROM area;
SELECT d.id_departamento, d.nombre, a.nombre AS area
FROM departamento d JOIN area a ON a.id_area = d.id_area;
SELECT * FROM horario;

-- Empleados con su departamento, area y horario
SELECT e.id_empleado, e.apellidos, e.nombres, e.cedula,
       d.nombre AS departamento, a.nombre AS area, h.nombre AS horario
FROM empleado e
JOIN departamento d ON d.id_departamento = e.id_departamento
JOIN area a ON a.id_area = d.id_area
JOIN horario h ON h.id_horario = e.id_horario;

-- Asistencias del dia
SELECT s.id_asistencia, s.id_empleado, e.apellidos, s.fecha,
       s.hora_entrada, s.hora_salida, s.estado_marcacion
FROM asistencia s JOIN empleado e ON e.id_empleado = s.id_empleado
WHERE s.fecha = CURRENT_DATE;
