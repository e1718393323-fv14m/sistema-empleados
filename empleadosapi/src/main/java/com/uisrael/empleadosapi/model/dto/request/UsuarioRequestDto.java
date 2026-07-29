package com.uisrael.empleadosapi.model.dto.request;

import lombok.Data;

@Data
public class UsuarioRequestDto {
	private Integer idUsuario;
	private String username;
	private String password;
	private Integer idEmpleado; // opcional
	private Integer idRol;
	private Boolean estado;
}
