package com.uisrael.empleadosweb.seguridad;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

/** Datos del usuario autenticado que viven en la sesion web */
@Data
@AllArgsConstructor
public class UsuarioSesion implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idUsuario;
	private String username;
	private String rol;
	private Integer idEmpleado;      // null si el usuario no esta vinculado a un empleado
	private String nombreEmpleado;

	@Override
	public String toString() {
		return username; // lo que muestra sec:authentication="name"
	}
}
