package com.uisrael.empleadosweb.services;

import java.util.List;

import com.uisrael.empleadosweb.model.dto.request.EmpleadoRequestDto;
import com.uisrael.empleadosweb.model.dto.response.EmpleadoResponseDto;

public interface IEmpleadoService {
	List<EmpleadoResponseDto> listar(String buscar);
	EmpleadoResponseDto buscarPorCodigo(Integer codigo);
	void guardar(EmpleadoRequestDto dto);
	void actualizar(Integer codigo, EmpleadoRequestDto dto);
	void eliminar(Integer codigo);
}
