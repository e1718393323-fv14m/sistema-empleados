package com.uisrael.empleadosapi.model.dto.request;

import java.time.LocalTime;

import lombok.Data;

@Data
public class HorarioRequestDto {
	private Integer idHorario;
	private String nombre;
	private LocalTime horaEntrada;
	private LocalTime horaSalida;
	private Integer toleranciaMinutos;
	private boolean estado;
}
