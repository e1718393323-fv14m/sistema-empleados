package com.uisrael.empleadosapi.model.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PermisoRequestDto {
	private Integer idEmpleado;
	private LocalDate fechaDesde;
	private LocalDate fechaHasta;
	private String tipoPermiso;
	private String motivo;
}
