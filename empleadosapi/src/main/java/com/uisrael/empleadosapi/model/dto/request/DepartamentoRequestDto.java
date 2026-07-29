package com.uisrael.empleadosapi.model.dto.request;

import lombok.Data;

@Data
public class DepartamentoRequestDto {
	private Integer idDepartamento;
	private String nombre;
	private String descripcion;
	private boolean estado;
	private Integer idArea; 
}
