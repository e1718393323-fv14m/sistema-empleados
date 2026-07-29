package com.uisrael.empleadosapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uisrael.empleadosapi.entities.Departamento;

public interface IDepartamentoRepository extends JpaRepository<Departamento, Integer> {

	@Query("SELECT d FROM Departamento d JOIN FETCH d.area ORDER BY d.nombre")
	List<Departamento> listarConArea();

	@Query("SELECT d FROM Departamento d JOIN FETCH d.area a "
		 + "WHERE a.idArea = :idArea AND d.estado = true")
	List<Departamento> buscarPorArea(@Param("idArea") Integer idArea);
}
