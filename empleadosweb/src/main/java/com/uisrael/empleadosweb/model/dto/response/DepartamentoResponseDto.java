package com.uisrael.empleadosweb.model.dto.response;

import lombok.Data;

@Data
public class DepartamentoResponseDto {
	private Integer idDepartamento;
	private String nombre;
	private String descripcion;
	private boolean estado;
	private Integer idArea;
	private String nombreArea;
}
