package com.uisrael.empleadosweb.model.dto.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

import lombok.Data;

@Data
public class HorarioRequestDto {
	private Integer idHorario;
	private String nombre;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime horaEntrada;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime horaSalida;
	private Integer toleranciaMinutos;
	private boolean estado;
}
