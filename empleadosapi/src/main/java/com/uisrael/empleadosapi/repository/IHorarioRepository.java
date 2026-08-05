package com.uisrael.empleadosapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.uisrael.empleadosapi.entities.Horario;

public interface IHorarioRepository extends JpaRepository<Horario, Integer> {

	@Query("SELECT h FROM Horario h WHERE h.estado = true ORDER BY h.horaEntrada")
	List<Horario> listarActivos();
}
