package com.uisrael.empleadosweb.services;

import java.util.List;

import com.uisrael.empleadosweb.model.dto.request.PermisoRequestDto;
import com.uisrael.empleadosweb.model.dto.request.ResolucionPermisoRequestDto;
import com.uisrael.empleadosweb.model.dto.response.PermisoResponseDto;

public interface IPermisoService {
	List<PermisoResponseDto> listar(String estado);
	List<PermisoResponseDto> listarPorEmpleado(Integer idEmpleado);
	PermisoResponseDto crear(PermisoRequestDto dto);
	PermisoResponseDto aprobar(Integer id, ResolucionPermisoRequestDto dto);
	PermisoResponseDto rechazar(Integer id, ResolucionPermisoRequestDto dto);
}
