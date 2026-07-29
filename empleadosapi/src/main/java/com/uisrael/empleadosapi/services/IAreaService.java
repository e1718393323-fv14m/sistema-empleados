package com.uisrael.empleadosapi.services;

import java.util.List;

import com.uisrael.empleadosapi.model.dto.request.AreaRequestDto;
import com.uisrael.empleadosapi.model.dto.response.AreaResponseDto;

public interface IAreaService {
	List<AreaResponseDto> listar();
	AreaResponseDto buscarPorId(Integer id);
	AreaResponseDto guardar(AreaRequestDto dto);
	AreaResponseDto actualizar(Integer id, AreaRequestDto dto);
	void eliminar(Integer id);
}
