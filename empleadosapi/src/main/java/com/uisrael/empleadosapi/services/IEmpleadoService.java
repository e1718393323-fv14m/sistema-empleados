package com.uisrael.empleadosapi.services;

import java.util.List;

import com.uisrael.empleadosapi.model.dto.request.EmpleadoRequestDto;
import com.uisrael.empleadosapi.model.dto.response.EmpleadoResponseDto;

public interface IEmpleadoService {
	List<EmpleadoResponseDto> listar();
	List<EmpleadoResponseDto> buscar(String texto);
	EmpleadoResponseDto buscarPorCodigo(Integer codigo);
	EmpleadoResponseDto guardar(EmpleadoRequestDto dto);
	EmpleadoResponseDto actualizar(Integer codigo, EmpleadoRequestDto dto);
	void eliminar(Integer codigo);
}
