package com.uisrael.empleadosweb.model.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class AsistenciaResponseDto {
	private Integer idAsistencia;
	private LocalDate fecha;
	private LocalTime horaEntrada;
	private LocalTime horaSalida;
	private String estadoMarcacion;
	private String observacion;
	private Integer idEmpleado;
	private String codigoAlterno;
	private String nombreEmpleado;
}
