package com.uisrael.empleadosweb.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import com.uisrael.empleadosweb.model.dto.request.EmpleadoRequestDto;
import com.uisrael.empleadosweb.model.dto.response.EmpleadoResponseDto;
import com.uisrael.empleadosweb.services.IEmpleadoService;

@Service
public class EmpleadoServiceImpl implements IEmpleadoService {

	private final WebClient webClient;

	public EmpleadoServiceImpl(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public List<EmpleadoResponseDto> listar(String buscar) {
		return webClient.get()
				.uri(uriBuilder -> construirUri(uriBuilder, buscar))
				.retrieve()
				.bodyToFlux(EmpleadoResponseDto.class).collectList().block();
	}

	private java.net.URI construirUri(UriBuilder uriBuilder, String buscar) {
		uriBuilder.path("/empleados");
		if (buscar != null && !buscar.isBlank()) {
			uriBuilder.queryParam("buscar", buscar);
		}
		return uriBuilder.build();
	}

	@Override
	public EmpleadoResponseDto buscarPorCodigo(Integer codigo) {
		return webClient.get().uri("/empleados/{codigo}", codigo).retrieve()
				.bodyToMono(EmpleadoResponseDto.class).block();
	}

	@Override
	public void guardar(EmpleadoRequestDto dto) {
		webClient.post().uri("/empleados").bodyValue(dto)
				.retrieve().toBodilessEntity().block();
	}

	@Override
	public void actualizar(Integer codigo, EmpleadoRequestDto dto) {
		webClient.put().uri("/empleados/{codigo}", codigo).bodyValue(dto)
				.retrieve().toBodilessEntity().block();
	}

	@Override
	public void eliminar(Integer codigo) {
		webClient.delete().uri("/empleados/{codigo}", codigo)
				.retrieve().toBodilessEntity().block();
	}
}
