package com.uisrael.empleadosweb.services;

import java.util.List;

import com.uisrael.empleadosweb.model.dto.request.DepartamentoRequestDto;
import com.uisrael.empleadosweb.model.dto.response.DepartamentoResponseDto;

public interface IDepartamentoService {
	List<DepartamentoResponseDto> listar();
	DepartamentoResponseDto buscarPorId(Integer id);
	void guardar(DepartamentoRequestDto dto);
	void actualizar(Integer id, DepartamentoRequestDto dto);
	void eliminar(Integer id);
}
