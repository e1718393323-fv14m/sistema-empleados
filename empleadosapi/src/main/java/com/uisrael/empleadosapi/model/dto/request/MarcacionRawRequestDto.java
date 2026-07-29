package com.uisrael.empleadosapi.model.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class MarcacionRawRequestDto {
	private String codigoAlterno;
	private LocalDate fecha;
	private LocalTime hora;
	private String estadoMarcacion;
}
