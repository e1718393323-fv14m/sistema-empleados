package com.uisrael.empleadosapi.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uisrael.empleadosapi.entities.Asistencia;

public interface IAsistenciaRepository extends JpaRepository<Asistencia, Integer> {

	// JPQL: todas las asistencias con los datos del empleado
	@Query("SELECT a FROM Asistencia a "
		 + "JOIN FETCH a.empleado e "
		 + "ORDER BY a.fecha DESC, a.horaEntrada DESC")
	List<Asistencia> listarConEmpleado();

	// JPQL: asistencia de un empleado en una fecha (para marcar salida)
	@Query("SELECT a FROM Asistencia a "
		 + "WHERE a.empleado.idEmpleado = :codigo AND a.fecha = :fecha")
	Optional<Asistencia> buscarPorEmpleadoYFecha(@Param("codigo") Integer codigo,
			@Param("fecha") LocalDate fecha);

	// JPQL: historial de asistencias de un empleado
	@Query("SELECT a FROM Asistencia a JOIN FETCH a.empleado e "
		 + "WHERE e.idEmpleado = :codigo ORDER BY a.fecha DESC")
	List<Asistencia> listarPorEmpleado(@Param("codigo") Integer codigo);

	// JPQL: asistencias entre fechas
	@Query("SELECT a FROM Asistencia a JOIN FETCH a.empleado e "
		 + "WHERE a.fecha BETWEEN :desde AND :hasta ORDER BY a.fecha DESC")
	List<Asistencia> listarPorRango(@Param("desde") LocalDate desde,
			@Param("hasta") LocalDate hasta);

	// JPQL: contar atrasos de un empleado
	@Query("SELECT COUNT(a) FROM Asistencia a "
		 + "WHERE a.empleado.idEmpleado = :codigo AND a.estadoMarcacion = 'ATRASO'")
	long contarAtrasos(@Param("codigo") Integer codigo);
}
