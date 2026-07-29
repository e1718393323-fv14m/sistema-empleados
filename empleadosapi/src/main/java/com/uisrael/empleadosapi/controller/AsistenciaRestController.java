package com.uisrael.empleadosapi.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.uisrael.empleadosapi.model.dto.request.AsistenciaRequestDto;
import com.uisrael.empleadosapi.model.dto.response.AsistenciaResponseDto;
import com.uisrael.empleadosapi.services.IAsistenciaService;

@RestController
@RequestMapping("/api/asistencias")
public class AsistenciaRestController {

	private final IAsistenciaService asistenciaService;

	public AsistenciaRestController(IAsistenciaService asistenciaService) {
		this.asistenciaService = asistenciaService;
	}

	@GetMapping
	public List<AsistenciaResponseDto> listar(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
		if (desde != null && hasta != null) {
			return asistenciaService.listarPorRango(desde, hasta);
		}
		return asistenciaService.listar();
	}

	@GetMapping("/empleado/{codigo}")
	public List<AsistenciaResponseDto> listarPorEmpleado(@PathVariable Integer codigo) {
		return asistenciaService.listarPorEmpleado(codigo);
	}

	// Marcar entrada (compara contra el horario 08:30 y calcula PUNTUAL/ATRASO)
	@PostMapping("/entrada")
	@ResponseStatus(HttpStatus.CREATED)
	public AsistenciaResponseDto marcarEntrada(@RequestBody AsistenciaRequestDto dto) {
		return asistenciaService.marcarEntrada(dto);
	}

	// Marcar salida del dia actual
	@PutMapping("/salida")
	public AsistenciaResponseDto marcarSalida(@RequestBody AsistenciaRequestDto dto) {
		return asistenciaService.marcarSalida(dto);
	}
}
