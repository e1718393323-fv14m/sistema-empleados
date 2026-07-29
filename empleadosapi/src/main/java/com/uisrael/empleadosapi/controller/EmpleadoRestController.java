package com.uisrael.empleadosapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.uisrael.empleadosapi.model.dto.request.EmpleadoRequestDto;
import com.uisrael.empleadosapi.model.dto.response.EmpleadoResponseDto;
import com.uisrael.empleadosapi.services.IEmpleadoService;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoRestController {

	private final IEmpleadoService empleadoService;

	public EmpleadoRestController(IEmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}

	@GetMapping
	public List<EmpleadoResponseDto> listar(@RequestParam(required = false) String buscar) {
		if (buscar != null && !buscar.isBlank()) {
			return empleadoService.buscar(buscar);
		}
		return empleadoService.listar();
	}

	@GetMapping("/{codigo}")
	public EmpleadoResponseDto buscarPorCodigo(@PathVariable Integer codigo) {
		return empleadoService.buscarPorCodigo(codigo);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EmpleadoResponseDto guardar(@RequestBody EmpleadoRequestDto dto) {
		return empleadoService.guardar(dto);
	}

	@PutMapping("/{codigo}")
	public EmpleadoResponseDto actualizar(@PathVariable Integer codigo,
			@RequestBody EmpleadoRequestDto dto) {
		return empleadoService.actualizar(codigo, dto);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Integer codigo) {
		empleadoService.eliminar(codigo);
	}
}
