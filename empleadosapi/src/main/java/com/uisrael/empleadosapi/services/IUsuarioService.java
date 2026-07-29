package com.uisrael.empleadosapi.services;

import java.util.List;

import com.uisrael.empleadosapi.model.dto.request.LoginRequestDto;
import com.uisrael.empleadosapi.model.dto.request.UsuarioRequestDto;
import com.uisrael.empleadosapi.model.dto.response.IngresoResponseDto;
import com.uisrael.empleadosapi.model.dto.response.UsuarioResponseDto;

public interface IUsuarioService {
	List<UsuarioResponseDto> listar();
	UsuarioResponseDto buscarPorId(Integer idUsuario);
	UsuarioResponseDto crear(UsuarioRequestDto dto);
	UsuarioResponseDto actualizar(Integer idUsuario, UsuarioRequestDto dto);
	void eliminar(Integer idUsuario);
	UsuarioResponseDto autenticar(LoginRequestDto dto);
	List<IngresoResponseDto> listarIngresos();
}
