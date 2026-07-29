package com.uisrael.empleadosapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.uisrael.empleadosapi.entities.Ingreso;

public interface IIngresoRepository extends JpaRepository<Ingreso, Long> {

	@Query("SELECT i FROM Ingreso i LEFT JOIN FETCH i.usuario "
		 + "ORDER BY i.fechaHora DESC LIMIT 100")
	List<Ingreso> listarUltimos();
}
