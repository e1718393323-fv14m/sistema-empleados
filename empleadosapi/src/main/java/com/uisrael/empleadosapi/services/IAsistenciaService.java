package com.uisrael.empleadosapi.services;

import java.time.LocalDate;
import java.util.List;

import com.uisrael.empleadosapi.model.dto.request.AsistenciaRequestDto;
import com.uisrael.empleadosapi.model.dto.response.AsistenciaResponseDto;

public interface IAsistenciaService {
	List<AsistenciaResponseDto> listar();
	List<AsistenciaResponseDto> listarPorEmpleado(Integer codigo);
	List<AsistenciaResponseDto> listarPorRango(LocalDate desde, LocalDate hasta);
	AsistenciaResponseDto marcarEntrada(AsistenciaRequestDto dto);
	AsistenciaResponseDto marcarSalida(AsistenciaRequestDto dto);
}
