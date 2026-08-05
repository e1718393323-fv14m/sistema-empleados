package com.uisrael.empleadosapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uisrael.empleadosapi.model.dto.request.LoginRequestDto;
import com.uisrael.empleadosapi.model.dto.request.UsuarioRequestDto;
import com.uisrael.empleadosapi.model.dto.response.IngresoResponseDto;
import com.uisrael.empleadosapi.model.dto.response.UsuarioResponseDto;
import com.uisrael.empleadosapi.services.IUsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {

	private final IUsuarioService usuarioService;

	public UsuarioRestController(IUsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@GetMapping
	public ResponseEntity<List<UsuarioResponseDto>> listar() {
		return ResponseEntity.ok(usuarioService.listar());
	}

	@PostMapping
	public ResponseEntity<UsuarioResponseDto> crear(@RequestBody UsuarioRequestDto dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(dto));
	}

	@GetMapping("/{id}")
	public ResponseEntity<UsuarioResponseDto> buscarPorId(@PathVariable Integer id) {
		return ResponseEntity.ok(usuarioService.buscarPorId(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<UsuarioResponseDto> actualizar(@PathVariable Integer id,
			@RequestBody UsuarioRequestDto dto) {
		return ResponseEntity.ok(usuarioService.actualizar(id, dto));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Integer id) {
		usuarioService.eliminar(id);
	}

	/** Autenticacion: valida credenciales y registra el ingreso (auditoria) */
	@PostMapping("/login")
	public ResponseEntity<UsuarioResponseDto> login(@RequestBody LoginRequestDto dto) {
		return ResponseEntity.ok(usuarioService.autenticar(dto));
	}

	@GetMapping("/ingresos")
	public ResponseEntity<List<IngresoResponseDto>> ingresos() {
		return ResponseEntity.ok(usuarioService.listarIngresos());
	}
}
