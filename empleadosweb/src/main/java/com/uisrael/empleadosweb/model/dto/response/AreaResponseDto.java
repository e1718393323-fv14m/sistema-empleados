package com.uisrael.empleadosweb.model.dto.response;

import lombok.Data;

@Data
public class AreaResponseDto {
	private Integer idArea;
	private String nombre;
	private String descripcion;
	private boolean estado;
}
