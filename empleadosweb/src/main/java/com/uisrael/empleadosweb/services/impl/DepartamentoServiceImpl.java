package com.uisrael.empleadosweb.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.empleadosweb.model.dto.request.DepartamentoRequestDto;
import com.uisrael.empleadosweb.model.dto.response.DepartamentoResponseDto;
import com.uisrael.empleadosweb.services.IDepartamentoService;

@Service
public class DepartamentoServiceImpl implements IDepartamentoService {

	private final WebClient webClient;

	public DepartamentoServiceImpl(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public List<DepartamentoResponseDto> listar() {
		return webClient.get().uri("/departamentos").retrieve()
				.bodyToFlux(DepartamentoResponseDto.class).collectList().block();
	}

	@Override
	public DepartamentoResponseDto buscarPorId(Integer id) {
		return webClient.get().uri("/departamentos/{id}", id).retrieve()
				.bodyToMono(DepartamentoResponseDto.class).block();
	}

	@Override
	public void guardar(DepartamentoRequestDto dto) {
		webClient.post().uri("/departamentos").bodyValue(dto)
				.retrieve().toBodilessEntity().block();
	}

	@Override
	public void actualizar(Integer id, DepartamentoRequestDto dto) {
		webClient.put().uri("/departamentos/{id}", id).bodyValue(dto)
				.retrieve().toBodilessEntity().block();
	}

	@Override
	public void eliminar(Integer id) {
		webClient.delete().uri("/departamentos/{id}", id)
				.retrieve().toBodilessEntity().block();
	}
}
