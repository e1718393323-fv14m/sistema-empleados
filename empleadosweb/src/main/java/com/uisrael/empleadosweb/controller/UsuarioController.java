package com.uisrael.empleadosweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.uisrael.empleadosweb.model.dto.request.UsuarioRequestDto;
import com.uisrael.empleadosweb.model.dto.response.UsuarioResponseDto;
import com.uisrael.empleadosweb.seguridad.UsuarioSesion;
import com.uisrael.empleadosweb.services.IEmpleadoService;
import com.uisrael.empleadosweb.services.IUsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController extends ControladorBase {

	private final IUsuarioService usuarioService;
	private final IEmpleadoService empleadoService;

	public UsuarioController(IUsuarioService usuarioService, IEmpleadoService empleadoService) {
		this.usuarioService = usuarioService;
		this.empleadoService = empleadoService;
	}

	@GetMapping("/listar")
	public String listar(Model model) {
		model.addAttribute("listausuarios", usuarioService.listar());
		return "usuarios/listar";
	}

	@GetMapping("/nuevo")
	public String nuevo(Model model) {
		model.addAttribute("usuario", new UsuarioRequestDto());
		model.addAttribute("listaempleados", empleadoService.listar(null));
		return "usuarios/crear";
	}

	@PostMapping("/guardar")
	public String guardar(@ModelAttribute UsuarioRequestDto usuario, RedirectAttributes flash) {
		return ejecutar(flash, "Usuario creado correctamente",
				"redirect:/usuarios/listar", () -> usuarioService.crear(usuario));
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable Integer id, Model model) {
		UsuarioResponseDto u = usuarioService.buscarPorId(id);
		UsuarioRequestDto usuario = new UsuarioRequestDto();
		usuario.setIdUsuario(u.getIdUsuario());
		usuario.setUsername(u.getUsername());
		usuario.setIdEmpleado(u.getIdEmpleado());
		usuario.setEstado(u.isEstado());
		model.addAttribute("rolActual", u.getRol());
		model.addAttribute("usuario", usuario);
		model.addAttribute("listaempleados", empleadoService.listar(null));
		return "usuarios/editar";
	}

	@PostMapping("/actualizar")
	public String actualizar(@ModelAttribute UsuarioRequestDto usuario, RedirectAttributes flash) {
		return ejecutar(flash, "Usuario actualizado correctamente",
				"redirect:/usuarios/listar",
				() -> usuarioService.actualizar(usuario.getIdUsuario(), usuario));
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Integer id, RedirectAttributes flash,
			@AuthenticationPrincipal UsuarioSesion sesion) {
		if (sesion != null && sesion.getIdUsuario().equals(id)) {
			flash.addFlashAttribute("error",
					"No puede eliminar el usuario con el que tiene la sesion iniciada");
			return "redirect:/usuarios/listar";
		}
		return ejecutar(flash, "Usuario eliminado correctamente",
				"redirect:/usuarios/listar", () -> {
					usuarioService.eliminar(id);
					return null;
				});
	}

	@GetMapping("/ingresos")
	public String ingresos(Model model) {
		model.addAttribute("listaingresos", usuarioService.listarIngresos());
		return "usuarios/ingresos";
	}
}
