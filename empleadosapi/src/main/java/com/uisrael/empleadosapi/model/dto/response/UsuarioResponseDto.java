package com.uisrael.empleadosapi.model.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UsuarioResponseDto {
	private Integer idUsuario;
	private String username;
	private String rol;
	private Integer idEmpleado;
	private String nombreEmpleado;
	private boolean estado;
	private LocalDateTime ultimoIngreso;
}
