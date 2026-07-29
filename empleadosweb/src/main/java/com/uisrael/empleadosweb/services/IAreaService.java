package com.uisrael.empleadosweb.services;

import java.util.List;

import com.uisrael.empleadosweb.model.dto.request.AreaRequestDto;
import com.uisrael.empleadosweb.model.dto.response.AreaResponseDto;

public interface IAreaService {
	List<AreaResponseDto> listar();
	AreaResponseDto buscarPorId(Integer id);
	void guardar(AreaRequestDto dto);
	void actualizar(Integer id, AreaRequestDto dto);
	void eliminar(Integer id);
}
