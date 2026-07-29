package com.uisrael.empleadosweb.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uisrael.empleadosweb.model.dto.request.AsistenciaRequestDto;
import com.uisrael.empleadosweb.seguridad.UsuarioSesion;
import com.uisrael.empleadosweb.services.IAsistenciaService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/marcacion-remota")
public class MarcacionRemotaController extends ControladorBase {

	private final IAsistenciaService asistenciaService;

	public MarcacionRemotaController(IAsistenciaService asistenciaService) {
		this.asistenciaService = asistenciaService;
	}

	@GetMapping
	public String pantalla(Model model, @AuthenticationPrincipal UsuarioSesion usuario) {
		model.addAttribute("usuario", usuario);
		model.addAttribute("sinEmpleado", usuario == null || usuario.getIdEmpleado() == null);
		return "marcacion/remota";
	}

	@PostMapping("/entrada")
	public String entrada(@RequestParam String motivo, RedirectAttributes flash,
			@AuthenticationPrincipal UsuarioSesion usuario, HttpServletRequest request) {
		return marcar(true, motivo, flash, usuario, request);
	}

	@PostMapping("/salida")
	public String salida(@RequestParam String motivo, RedirectAttributes flash,
			@AuthenticationPrincipal UsuarioSesion usuario, HttpServletRequest request) {
		return marcar(false, motivo, flash, usuario, request);
	}

	private String marcar(boolean entrada, String motivo, RedirectAttributes flash,
			UsuarioSesion usuario, HttpServletRequest request) {
		if (usuario.getIdEmpleado() == null) {
			flash.addFlashAttribute("error",
					"Su usuario no esta vinculado a un empleado; contacte al administrador");
			return "redirect:/marcacion-remota";
		}
		if (motivo == null || motivo.isBlank()) {
			flash.addFlashAttribute("error", "El motivo de la marcacion remota es obligatorio");
			return "redirect:/marcacion-remota";
		}
		AsistenciaRequestDto dto = new AsistenciaRequestDto();
		dto.setIdEmpleado(usuario.getIdEmpleado());
		dto.setObservacion("REMOTA: " + motivo.trim() + " (IP: " + request.getRemoteAddr() + ")");
		String msg = entrada ? "Entrada remota registrada" : "Salida remota registrada";
		return ejecutar(flash, msg, "redirect:/marcacion-remota", () -> {
			if (entrada) asistenciaService.marcarEntrada(dto);
			else asistenciaService.marcarSalida(dto);
			return null;
		});
	}
}
