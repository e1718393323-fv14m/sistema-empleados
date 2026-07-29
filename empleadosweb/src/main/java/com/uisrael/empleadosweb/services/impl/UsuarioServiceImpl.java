package com.uisrael.empleadosweb.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.empleadosweb.model.dto.request.LoginRequestDto;
import com.uisrael.empleadosweb.model.dto.request.UsuarioRequestDto;
import com.uisrael.empleadosweb.model.dto.response.IngresoResponseDto;
import com.uisrael.empleadosweb.model.dto.response.UsuarioResponseDto;
import com.uisrael.empleadosweb.services.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

	private final WebClient webClient;

	public UsuarioServiceImpl(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public UsuarioResponseDto login(LoginRequestDto dto) {
		return webClient.post().uri("/usuarios/login").bodyValue(dto).retrieve()
				.bodyToMono(UsuarioResponseDto.class).block();
	}

	@Override
	public List<UsuarioResponseDto> listar() {
		return webClient.get().uri("/usuarios").retrieve()
				.bodyToFlux(UsuarioResponseDto.class).collectList().block();
	}

	@Override
	public UsuarioResponseDto crear(UsuarioRequestDto dto) {
		return webClient.post().uri("/usuarios").bodyValue(dto).retrieve()
				.bodyToMono(UsuarioResponseDto.class).block();
	}

	@Override
	public UsuarioResponseDto buscarPorId(Integer idUsuario) {
		return webClient.get().uri("/usuarios/" + idUsuario).retrieve()
				.bodyToMono(UsuarioResponseDto.class).block();
	}

	@Override
	public UsuarioResponseDto actualizar(Integer idUsuario, UsuarioRequestDto dto) {
		return webClient.put().uri("/usuarios/" + idUsuario).bodyValue(dto).retrieve()
				.bodyToMono(UsuarioResponseDto.class).block();
	}

	@Override
	public void eliminar(Integer idUsuario) {
		webClient.delete().uri("/usuarios/" + idUsuario).retrieve()
				.toBodilessEntity().block();
	}

	@Override
	public List<IngresoResponseDto> listarIngresos() {
		return webClient.get().uri("/usuarios/ingresos").retrieve()
				.bodyToFlux(IngresoResponseDto.class).collectList().block();
	}
}
