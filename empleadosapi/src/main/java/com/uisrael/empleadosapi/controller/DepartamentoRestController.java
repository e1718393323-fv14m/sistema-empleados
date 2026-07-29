package com.uisrael.empleadosapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.uisrael.empleadosapi.model.dto.request.DepartamentoRequestDto;
import com.uisrael.empleadosapi.model.dto.response.DepartamentoResponseDto;
import com.uisrael.empleadosapi.services.IDepartamentoService;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoRestController {

	private final IDepartamentoService departamentoService;

	public DepartamentoRestController(IDepartamentoService departamentoService) {
		this.departamentoService = departamentoService;
	}

	@GetMapping
	public List<DepartamentoResponseDto> listar() {
		return departamentoService.listar();
	}

	@GetMapping("/{id}")
	public DepartamentoResponseDto buscar(@PathVariable Integer id) {
		return departamentoService.buscarPorId(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public DepartamentoResponseDto guardar(@RequestBody DepartamentoRequestDto dto) {
		return departamentoService.guardar(dto);
	}

	@PutMapping("/{id}")
	public DepartamentoResponseDto actualizar(@PathVariable Integer id,
			@RequestBody DepartamentoRequestDto dto) {
		return departamentoService.actualizar(id, dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Integer id) {
		departamentoService.eliminar(id);
	}
}
