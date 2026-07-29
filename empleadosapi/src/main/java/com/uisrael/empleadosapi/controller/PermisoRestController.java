package com.uisrael.empleadosapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uisrael.empleadosapi.model.dto.request.PermisoRequestDto;
import com.uisrael.empleadosapi.model.dto.request.ResolucionPermisoRequestDto;
import com.uisrael.empleadosapi.model.dto.response.PermisoResponseDto;
import com.uisrael.empleadosapi.services.IPermisoService;

@RestController
@RequestMapping("/api/permisos")
public class PermisoRestController {

	private final IPermisoService permisoService;

	public PermisoRestController(IPermisoService permisoService) {
		this.permisoService = permisoService;
	}

	@GetMapping
	public ResponseEntity<List<PermisoResponseDto>> listar(
			@RequestParam(required = false) String estado,
			@RequestParam(required = false) Integer empleado) {
		return ResponseEntity.ok(permisoService.listar(estado, empleado));
	}

	@PostMapping
	public ResponseEntity<PermisoResponseDto> crear(@RequestBody PermisoRequestDto dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(permisoService.crear(dto));
	}

	@PutMapping("/{id}/aprobar")
	public ResponseEntity<PermisoResponseDto> aprobar(@PathVariable Integer id,
			@RequestBody ResolucionPermisoRequestDto dto) {
		return ResponseEntity.ok(permisoService.aprobar(id, dto));
	}

	@PutMapping("/{id}/rechazar")
	public ResponseEntity<PermisoResponseDto> rechazar(@PathVariable Integer id,
			@RequestBody ResolucionPermisoRequestDto dto) {
		return ResponseEntity.ok(permisoService.rechazar(id, dto));
	}
}
