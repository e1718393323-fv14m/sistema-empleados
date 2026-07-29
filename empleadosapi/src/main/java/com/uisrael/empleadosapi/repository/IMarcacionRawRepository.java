package com.uisrael.empleadosapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uisrael.empleadosapi.entities.MarcacionRaw;

public interface IMarcacionRawRepository extends JpaRepository<MarcacionRaw, Long> {

	// JPQL: marcaciones pendientes de procesar
	@Query("SELECT m FROM MarcacionRaw m WHERE m.procesado = false "
		 + "ORDER BY m.codigoAlterno, m.fecha, m.hora")
	List<MarcacionRaw> listarPendientes();

	// JPQL: pendientes de una fecha especifica
	@Query("SELECT m FROM MarcacionRaw m WHERE m.procesado = false AND m.fecha = :fecha "
		 + "ORDER BY m.codigoAlterno, m.hora")
	List<MarcacionRaw> listarPendientesPorFecha(@Param("fecha") LocalDate fecha);
}
