package com.uisrael.empleadosweb.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.empleadosweb.model.dto.request.AsistenciaRequestDto;
import com.uisrael.empleadosweb.model.dto.response.AsistenciaResponseDto;
import com.uisrael.empleadosweb.services.IAsistenciaService;

@Service
public class AsistenciaServiceImpl implements IAsistenciaService {

	private final WebClient webClient;

	public AsistenciaServiceImpl(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public List<AsistenciaResponseDto> listar() {
		return webClient.get().uri("/asistencias").retrieve()
				.bodyToFlux(AsistenciaResponseDto.class).collectList().block();
	}

	@Override
	public List<AsistenciaResponseDto> listarPorEmpleado(Integer codigo) {
		return webClient.get().uri("/asistencias/empleado/{codigo}", codigo).retrieve()
				.bodyToFlux(AsistenciaResponseDto.class).collectList().block();
	}

	@Override
	public void marcarEntrada(AsistenciaRequestDto dto) {
		webClient.post().uri("/asistencias/entrada").bodyValue(dto)
				.retrieve().toBodilessEntity().block();
	}

	@Override
	public void marcarSalida(AsistenciaRequestDto dto) {
		webClient.put().uri("/asistencias/salida").bodyValue(dto)
				.retrieve().toBodilessEntity().block();
	}
}
