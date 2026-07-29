package com.uisrael.empleadosweb.services;

import java.util.List;

import com.uisrael.empleadosweb.model.dto.request.HorarioRequestDto;
import com.uisrael.empleadosweb.model.dto.response.HorarioResponseDto;

public interface IHorarioService {
	List<HorarioResponseDto> listar();
	HorarioResponseDto buscarPorId(Integer id);
	void guardar(HorarioRequestDto dto);
	void actualizar(Integer id, HorarioRequestDto dto);
	void eliminar(Integer id);
}
