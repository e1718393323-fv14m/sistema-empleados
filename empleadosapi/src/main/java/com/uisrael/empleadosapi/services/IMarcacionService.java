package com.uisrael.empleadosapi.services;

import java.time.LocalDate;
import java.util.List;

import com.uisrael.empleadosapi.model.dto.request.MarcacionRawRequestDto;
import com.uisrael.empleadosapi.model.dto.response.ProcesoMarcacionResponseDto;

public interface IMarcacionService {

	/** Carga masiva de marcaciones crudas (integracion de datos) */
	int cargar(List<MarcacionRawRequestDto> marcaciones);

	/** Consolida las marcaciones pendientes en la tabla asistencias */
	ProcesoMarcacionResponseDto procesar(LocalDate fecha);
}
