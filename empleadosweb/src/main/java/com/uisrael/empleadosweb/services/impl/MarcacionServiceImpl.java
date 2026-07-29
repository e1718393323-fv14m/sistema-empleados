package com.uisrael.empleadosweb.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.uisrael.empleadosweb.model.dto.response.ProcesoMarcacionResponseDto;
import com.uisrael.empleadosweb.services.IMarcacionService;

@Service
public class MarcacionServiceImpl implements IMarcacionService {

	private final WebClient webClient;

	public MarcacionServiceImpl(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public ProcesoMarcacionResponseDto procesar() {
		return webClient.post().uri("/marcaciones/procesar").retrieve()
				.bodyToMono(ProcesoMarcacionResponseDto.class).block();
	}
}
