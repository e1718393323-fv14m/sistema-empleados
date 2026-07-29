package com.uisrael.empleadosweb.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.empleadosweb.model.dto.request.PermisoRequestDto;
import com.uisrael.empleadosweb.model.dto.request.ResolucionPermisoRequestDto;
import com.uisrael.empleadosweb.model.dto.response.PermisoResponseDto;
import com.uisrael.empleadosweb.services.IPermisoService;

@Service
public class PermisoServiceImpl implements IPermisoService {

	private final WebClient webClient;

	public PermisoServiceImpl(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public List<PermisoResponseDto> listar(String estado) {
		String uri = (estado == null || estado.isBlank()) ? "/permisos"
				: "/permisos?estado=" + estado;
		return webClient.get().uri(uri).retrieve()
				.bodyToFlux(PermisoResponseDto.class).collectList().block();
	}

	@Override
	public List<PermisoResponseDto> listarPorEmpleado(Integer idEmpleado) {
		return webClient.get().uri("/permisos?empleado=" + idEmpleado).retrieve()
				.bodyToFlux(PermisoResponseDto.class).collectList().block();
	}

	@Override
	public PermisoResponseDto crear(PermisoRequestDto dto) {
		return webClient.post().uri("/permisos").bodyValue(dto).retrieve()
				.bodyToMono(PermisoResponseDto.class).block();
	}

	@Override
	public PermisoResponseDto aprobar(Integer id, ResolucionPermisoRequestDto dto) {
		return webClient.put().uri("/permisos/" + id + "/aprobar").bodyValue(dto).retrieve()
				.bodyToMono(PermisoResponseDto.class).block();
	}

	@Override
	public PermisoResponseDto rechazar(Integer id, ResolucionPermisoRequestDto dto) {
		return webClient.put().uri("/permisos/" + id + "/rechazar").bodyValue(dto).retrieve()
				.bodyToMono(PermisoResponseDto.class).block();
	}
}
