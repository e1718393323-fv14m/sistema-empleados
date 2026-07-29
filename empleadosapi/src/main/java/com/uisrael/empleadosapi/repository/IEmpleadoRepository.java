package com.uisrael.empleadosapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uisrael.empleadosapi.entities.Empleado;

public interface IEmpleadoRepository extends JpaRepository<Empleado, Integer> {

	@Query("SELECT e FROM Empleado e "
		 + "JOIN FETCH e.departamento d "
		 + "JOIN FETCH d.area "
		 + "JOIN FETCH e.horario "
		 + "ORDER BY e.apellidos")
	List<Empleado> listarConRelaciones();

	@Query("SELECT e FROM Empleado e "
		 + "JOIN FETCH e.departamento d "
		 + "JOIN FETCH d.area "
		 + "JOIN FETCH e.horario "
		 + "WHERE e.idEmpleado = :codigo")
	Optional<Empleado> buscarPorCodigo(@Param("codigo") Integer codigo);

	@Query("SELECT e FROM Empleado e WHERE e.cedula = :cedula")
	Optional<Empleado> buscarPorCedula(@Param("cedula") String cedula);

	@Query("SELECT e FROM Empleado e JOIN FETCH e.horario "
		 + "WHERE e.codigoAlterno = :codigoAlterno")
	Optional<Empleado> buscarPorCodigoAlterno(@Param("codigoAlterno") String codigoAlterno);

	@Query("SELECT e FROM Empleado e "
		 + "JOIN FETCH e.departamento d "
		 + "JOIN FETCH d.area "
		 + "JOIN FETCH e.horario "
		 + "WHERE UPPER(e.apellidos) LIKE UPPER(CONCAT('%', :texto, '%')) "
		 + "   OR UPPER(e.nombres)  LIKE UPPER(CONCAT('%', :texto, '%'))")
	List<Empleado> buscarPorNombreOApellido(@Param("texto") String texto);

	@Query("SELECT COUNT(e) FROM Empleado e "
		 + "WHERE e.departamento.idDepartamento = :idDepartamento AND e.estado = true")
	long contarPorDepartamento(@Param("idDepartamento") Integer idDepartamento);
}
