package com.uisrael.empleadosapi.services;

import java.util.List;

import com.uisrael.empleadosapi.model.dto.request.PermisoRequestDto;
import com.uisrael.empleadosapi.model.dto.request.ResolucionPermisoRequestDto;
import com.uisrael.empleadosapi.model.dto.response.PermisoResponseDto;

public interface IPermisoService {
	List<PermisoResponseDto> listar(String estado, Integer idEmpleado);
	PermisoResponseDto crear(PermisoRequestDto dto);
	PermisoResponseDto aprobar(Integer idPermiso, ResolucionPermisoRequestDto dto);
	PermisoResponseDto rechazar(Integer idPermiso, ResolucionPermisoRequestDto dto);
}
