package com.uisrael.empleadosweb.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.empleadosweb.model.dto.request.AreaRequestDto;
import com.uisrael.empleadosweb.model.dto.response.AreaResponseDto;
import com.uisrael.empleadosweb.services.IAreaService;

@Service
public class AreaServiceImpl implements IAreaService {

	private final WebClient webClient;

	public AreaServiceImpl(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public List<AreaResponseDto> listar() {
		return webClient.get().uri("/areas").retrieve()
				.bodyToFlux(AreaResponseDto.class).collectList().block();
	}

	@Override
	public AreaResponseDto buscarPorId(Integer id) {
		return webClient.get().uri("/areas/{id}", id).retrieve()
				.bodyToMono(AreaResponseDto.class).block();
	}

	@Override
	public void guardar(AreaRequestDto dto) {
		webClient.post().uri("/areas").bodyValue(dto)
				.retrieve().toBodilessEntity().block();
	}

	@Override
	public void actualizar(Integer id, AreaRequestDto dto) {
		webClient.put().uri("/areas/{id}", id).bodyValue(dto)
				.retrieve().toBodilessEntity().block();
	}

	@Override
	public void eliminar(Integer id) {
		webClient.delete().uri("/areas/{id}", id)
				.retrieve().toBodilessEntity().block();
	}
}
