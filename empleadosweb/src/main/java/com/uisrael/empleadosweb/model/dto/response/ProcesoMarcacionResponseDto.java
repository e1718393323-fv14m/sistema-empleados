package com.uisrael.empleadosweb.model.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ProcesoMarcacionResponseDto {
	private int marcacionesLeidas;
	private int asistenciasCreadas;
	private int asistenciasActualizadas;
	private int sinEmpleado;
	private List<String> codigosSinMatch = new ArrayList<>();
}
