package com.uisrael.empleadosapi.model.dto.request;

import lombok.Data;

@Data
public class AsistenciaRequestDto {
	private Integer idEmpleado; // codigo del empleado (ficha principal)
	private String observacion;
}
