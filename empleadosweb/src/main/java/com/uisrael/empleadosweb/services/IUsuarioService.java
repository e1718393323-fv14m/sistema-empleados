package com.uisrael.empleadosweb.services;

import java.util.List;

import com.uisrael.empleadosweb.model.dto.request.LoginRequestDto;
import com.uisrael.empleadosweb.model.dto.request.UsuarioRequestDto;
import com.uisrael.empleadosweb.model.dto.response.IngresoResponseDto;
import com.uisrael.empleadosweb.model.dto.response.UsuarioResponseDto;

public interface IUsuarioService {
	UsuarioResponseDto login(LoginRequestDto dto);
	List<UsuarioResponseDto> listar();
	UsuarioResponseDto buscarPorId(Integer idUsuario);
	UsuarioResponseDto crear(UsuarioRequestDto dto);
	UsuarioResponseDto actualizar(Integer idUsuario, UsuarioRequestDto dto);
	void eliminar(Integer idUsuario);
	List<IngresoResponseDto> listarIngresos();
}
