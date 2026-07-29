package com.uisrael.empleadosapi.services;

import java.util.List;

import com.uisrael.empleadosapi.model.dto.request.DepartamentoRequestDto;
import com.uisrael.empleadosapi.model.dto.response.DepartamentoResponseDto;

public interface IDepartamentoService {
	List<DepartamentoResponseDto> listar();
	DepartamentoResponseDto buscarPorId(Integer id);
	DepartamentoResponseDto guardar(DepartamentoRequestDto dto);
	DepartamentoResponseDto actualizar(Integer id, DepartamentoRequestDto dto);
	void eliminar(Integer id);
}
