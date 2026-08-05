package com.uisrael.empleadosapi.model.dto.request;

import lombok.Data;

@Data
public class ResolucionPermisoRequestDto {
	private Integer idUsuario; 
	private String observacion;
}
