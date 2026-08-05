package com.uisrael.empleadosapi.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uisrael.empleadosapi.entities.Asistencia;

public interface IAsistenciaRepository extends JpaRepository<Asistencia, Integer> {

	@Query("SELECT a FROM Asistencia a "
		 + "JOIN FETCH a.empleado e "
		 + "ORDER BY a.fecha DESC, a.horaEntrada DESC")
	List<Asistencia> listarConEmpleado();

	@Query("SELECT a FROM Asistencia a "
		 + "WHERE a.empleado.idEmpleado = :codigo AND a.fecha = :fecha")
	Optional<Asistencia> buscarPorEmpleadoYFecha(@Param("codigo") Integer codigo,
			@Param("fecha") LocalDate fecha);

	@Query("SELECT a FROM Asistencia a JOIN FETCH a.empleado e "
		 + "WHERE e.idEmpleado = :codigo ORDER BY a.fecha DESC")
	List<Asistencia> listarPorEmpleado(@Param("codigo") Integer codigo);

	@Query("SELECT a FROM Asistencia a JOIN FETCH a.empleado e "
		 + "WHERE a.fecha BETWEEN :desde AND :hasta ORDER BY a.fecha DESC")
	List<Asistencia> listarPorRango(@Param("desde") LocalDate desde,
			@Param("hasta") LocalDate hasta);

	@Query("SELECT COUNT(a) FROM Asistencia a "
		 + "WHERE a.empleado.idEmpleado = :codigo AND a.estadoMarcacion = 'ATRASO'")
	long contarAtrasos(@Param("codigo") Integer codigo);
}
