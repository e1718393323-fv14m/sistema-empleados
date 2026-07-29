package com.uisrael.empleadosapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uisrael.empleadosapi.entities.Rol;

public interface IRolRepository extends JpaRepository<Rol, Integer> {

	@Query("SELECT r FROM Rol r WHERE r.nombre = :nombre")
	Optional<Rol> buscarPorNombre(@Param("nombre") String nombre);
}
