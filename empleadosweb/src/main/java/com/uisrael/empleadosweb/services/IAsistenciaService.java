package com.uisrael.empleadosweb.services;

import java.util.List;

import com.uisrael.empleadosweb.model.dto.request.AsistenciaRequestDto;
import com.uisrael.empleadosweb.model.dto.response.AsistenciaResponseDto;

public interface IAsistenciaService {
	List<AsistenciaResponseDto> listar();
	List<AsistenciaResponseDto> listarPorEmpleado(Integer codigo);
	void marcarEntrada(AsistenciaRequestDto dto);
	void marcarSalida(AsistenciaRequestDto dto);
}
