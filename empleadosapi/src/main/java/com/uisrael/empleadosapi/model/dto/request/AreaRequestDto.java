package com.uisrael.empleadosapi.model.dto.request;

import lombok.Data;

@Data
public class AreaRequestDto {
	private Integer idArea;
	private String nombre;
	private String descripcion;
	private boolean estado;
}
