package com.uisrael.empleadosweb.model.dto.response;

import java.time.LocalTime;

import lombok.Data;

@Data
public class HorarioResponseDto {
	private Integer idHorario;
	private String nombre;
	private LocalTime horaEntrada;
	private LocalTime horaSalida;
	private Integer toleranciaMinutos;
	private boolean estado;
}
