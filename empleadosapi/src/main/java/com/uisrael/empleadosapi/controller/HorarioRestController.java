package com.uisrael.empleadosapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.uisrael.empleadosapi.model.dto.request.HorarioRequestDto;
import com.uisrael.empleadosapi.model.dto.response.HorarioResponseDto;
import com.uisrael.empleadosapi.services.IHorarioService;

@RestController
@RequestMapping("/api/horarios")
public class HorarioRestController {

	private final IHorarioService horarioService;

	public HorarioRestController(IHorarioService horarioService) {
		this.horarioService = horarioService;
	}

	@GetMapping
	public List<HorarioResponseDto> listar() {
		return horarioService.listar();
	}

	@GetMapping("/{id}")
	public HorarioResponseDto buscar(@PathVariable Integer id) {
		return horarioService.buscarPorId(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public HorarioResponseDto guardar(@RequestBody HorarioRequestDto dto) {
		return horarioService.guardar(dto);
	}

	@PutMapping("/{id}")
	public HorarioResponseDto actualizar(@PathVariable Integer id, @RequestBody HorarioRequestDto dto) {
		return horarioService.actualizar(id, dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Integer id) {
		horarioService.eliminar(id);
	}
}
