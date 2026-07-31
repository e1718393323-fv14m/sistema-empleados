package com.uisrael.empleadosapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.uisrael.empleadosapi.entities.Area;

public interface IAreaRepository extends JpaRepository<Area, Integer> {

	@Query("SELECT a FROM Area a WHERE a.estado = true ORDER BY a.nombre")
	List<Area> listarActivas();
}
