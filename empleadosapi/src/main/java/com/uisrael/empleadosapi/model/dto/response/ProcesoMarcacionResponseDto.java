package com.uisrael.empleadosapi.model.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/** Resumen del proceso de consolidacion de marcaciones */
@Data
public class ProcesoMarcacionResponseDto {
	private int marcacionesLeidas;
	private int asistenciasCreadas;
	private int asistenciasActualizadas;
	private int sinEmpleado; // codigos alternos que no hicieron match
	private List<String> codigosSinMatch = new ArrayList<>();
}
