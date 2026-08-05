package com.uisrael.empleadosweb.seguridad;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioSesion implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idUsuario;
	private String username;
	private String rol;
	private Integer idEmpleado; 
	private String nombreEmpleado;

	@Override
	public String toString() {
		return username;
	}
}
