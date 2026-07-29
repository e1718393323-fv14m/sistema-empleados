package com.uisrael.empleadosapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.uisrael.empleadosapi.model.dto.request.AreaRequestDto;
import com.uisrael.empleadosapi.model.dto.response.AreaResponseDto;
import com.uisrael.empleadosapi.services.IAreaService;

@RestController
@RequestMapping("/api/areas")
public class AreaRestController {

	private final IAreaService areaService;

	public AreaRestController(IAreaService areaService) {
		this.areaService = areaService;
	}

	@GetMapping
	public List<AreaResponseDto> listar() {
		return areaService.listar();
	}

	@GetMapping("/{id}")
	public AreaResponseDto buscar(@PathVariable Integer id) {
		return areaService.buscarPorId(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public AreaResponseDto guardar(@RequestBody AreaRequestDto dto) {
		return areaService.guardar(dto);
	}

	@PutMapping("/{id}")
	public AreaResponseDto actualizar(@PathVariable Integer id, @RequestBody AreaRequestDto dto) {
		return areaService.actualizar(id, dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Integer id) {
		areaService.eliminar(id);
	}
}
