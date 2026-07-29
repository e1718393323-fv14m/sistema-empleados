package com.uisrael.empleadosapi.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uisrael.empleadosapi.entities.Permiso;

public interface IPermisoRepository extends JpaRepository<Permiso, Integer> {

	@Query("SELECT p FROM Permiso p JOIN FETCH p.empleado "
		 + "ORDER BY p.fechaSolicitud DESC")
	List<Permiso> listarConEmpleado();

	@Query("SELECT p FROM Permiso p JOIN FETCH p.empleado "
		 + "WHERE p.estado = :estado ORDER BY p.fechaSolicitud DESC")
	List<Permiso> listarPorEstado(@Param("estado") String estado);

	@Query("SELECT p FROM Permiso p JOIN FETCH p.empleado "
		 + "WHERE p.empleado.idEmpleado = :idEmpleado ORDER BY p.fechaSolicitud DESC")
	List<Permiso> listarPorEmpleado(@Param("idEmpleado") Integer idEmpleado);

	@Query("SELECT p FROM Permiso p WHERE p.empleado.idEmpleado = :idEmpleado "
		 + "AND p.estado = 'APROBADO' AND :fecha BETWEEN p.fechaDesde AND p.fechaHasta "
		 + "ORDER BY p.idPermiso LIMIT 1")
	Optional<Permiso> buscarAprobadoVigente(@Param("idEmpleado") Integer idEmpleado,
			@Param("fecha") LocalDate fecha);
}
