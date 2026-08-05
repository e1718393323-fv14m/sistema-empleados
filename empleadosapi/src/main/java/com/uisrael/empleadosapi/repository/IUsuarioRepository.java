package com.uisrael.empleadosapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uisrael.empleadosapi.entities.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

	@Query("SELECT u FROM Usuario u JOIN FETCH u.rol LEFT JOIN FETCH u.empleado "
		 + "WHERE u.username = :username")
	Optional<Usuario> buscarPorUsername(@Param("username") String username);

	@Query("SELECT u FROM Usuario u JOIN FETCH u.rol LEFT JOIN FETCH u.empleado "
		 + "ORDER BY u.username")
	List<Usuario> listarConRelaciones();
}
