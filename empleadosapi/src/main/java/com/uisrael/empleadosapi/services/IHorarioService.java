package com.uisrael.empleadosapi.services;

import java.util.List;

import com.uisrael.empleadosapi.model.dto.request.HorarioRequestDto;
import com.uisrael.empleadosapi.model.dto.response.HorarioResponseDto;

public interface IHorarioService {
	List<HorarioResponseDto> listar();
	HorarioResponseDto buscarPorId(Integer id);
	HorarioResponseDto guardar(HorarioRequestDto dto);
	HorarioResponseDto actualizar(Integer id, HorarioRequestDto dto);
	void eliminar(Integer id);
}
