package com.uisrael.empleadosweb.model.dto.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class PermisoRequestDto {
	private Integer idEmpleado;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate fechaDesde;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate fechaHasta;
	private String tipoPermiso;
	private String motivo;
}
