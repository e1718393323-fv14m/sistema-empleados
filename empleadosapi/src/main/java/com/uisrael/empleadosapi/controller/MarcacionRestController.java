package com.uisrael.empleadosapi.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uisrael.empleadosapi.model.dto.request.MarcacionRawRequestDto;
import com.uisrael.empleadosapi.model.dto.response.ProcesoMarcacionResponseDto;
import com.uisrael.empleadosapi.services.IMarcacionService;

@RestController
@RequestMapping("/api/marcaciones")
public class MarcacionRestController {

	private final IMarcacionService marcacionService;

	public MarcacionRestController(IMarcacionService marcacionService) {
		this.marcacionService = marcacionService;
	}

	/** Carga masiva de marcaciones crudas (integracion) */
	@PostMapping
	public ResponseEntity<String> cargar(@RequestBody List<MarcacionRawRequestDto> marcaciones) {
		int n = marcacionService.cargar(marcaciones);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body("Se cargaron " + n + " marcaciones");
	}

	/** Consolida marcaciones pendientes en asistencias (opcional ?fecha=yyyy-MM-dd) */
	@PostMapping("/procesar")
	public ResponseEntity<ProcesoMarcacionResponseDto> procesar(
			@RequestParam(required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
		return ResponseEntity.ok(marcacionService.procesar(fecha));
	}
}
