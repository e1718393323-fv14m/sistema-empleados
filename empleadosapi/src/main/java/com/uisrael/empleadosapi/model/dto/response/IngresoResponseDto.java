package com.uisrael.empleadosapi.model.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class IngresoResponseDto {
	private Long idIngreso;
	private String username;
	private LocalDateTime fechaHora;
	private String ip;
	private boolean exitoso;
}
