package com.uisrael.empleadosapi.model.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PermisoResponseDto {
	private Integer idPermiso;
	private Integer idEmpleado;
	private String nombreEmpleado;
	private LocalDateTime fechaSolicitud;
	private LocalDate fechaDesde;
	private LocalDate fechaHasta;
	private String tipoPermiso;
	private String motivo;
	private String estado;
	private String aprobadoPor;
	private LocalDateTime fechaResolucion;
	private String observacionRrhh;
}
