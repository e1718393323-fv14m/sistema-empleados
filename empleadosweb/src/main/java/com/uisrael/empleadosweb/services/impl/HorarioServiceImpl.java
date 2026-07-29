package com.uisrael.empleadosweb.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.empleadosweb.model.dto.request.HorarioRequestDto;
import com.uisrael.empleadosweb.model.dto.response.HorarioResponseDto;
import com.uisrael.empleadosweb.services.IHorarioService;

@Service
public class HorarioServiceImpl implements IHorarioService {

	private final WebClient webClient;

	public HorarioServiceImpl(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public List<HorarioResponseDto> listar() {
		return webClient.get().uri("/horarios").retrieve()
				.bodyToFlux(HorarioResponseDto.class).collectList().block();
	}

	@Override
	public HorarioResponseDto buscarPorId(Integer id) {
		return webClient.get().uri("/horarios/{id}", id).retrieve()
				.bodyToMono(HorarioResponseDto.class).block();
	}

	@Override
	public void guardar(HorarioRequestDto dto) {
		webClient.post().uri("/horarios").bodyValue(dto)
				.retrieve().toBodilessEntity().block();
	}

	@Override
	public void actualizar(Integer id, HorarioRequestDto dto) {
		webClient.put().uri("/horarios/{id}", id).bodyValue(dto)
				.retrieve().toBodilessEntity().block();
	}

	@Override
	public void eliminar(Integer id) {
		webClient.delete().uri("/horarios/{id}", id)
				.retrieve().toBodilessEntity().block();
	}
}
