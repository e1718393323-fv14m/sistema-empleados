package com.uisrael.empleadosweb.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uisrael.empleadosweb.model.dto.request.PermisoRequestDto;
import com.uisrael.empleadosweb.model.dto.request.ResolucionPermisoRequestDto;
import com.uisrael.empleadosweb.seguridad.UsuarioSesion;
import com.uisrael.empleadosweb.services.IEmpleadoService;
import com.uisrael.empleadosweb.services.IPermisoService;

@Controller
@RequestMapping("/permisos")
public class PermisoController extends ControladorBase {

	private final IPermisoService permisoService;
	private final IEmpleadoService empleadoService;

	public PermisoController(IPermisoService permisoService, IEmpleadoService empleadoService) {
		this.permisoService = permisoService;
		this.empleadoService = empleadoService;
	}

	@GetMapping("/listar")
	public String listar(@RequestParam(required = false) String estado, Model model,
			@AuthenticationPrincipal UsuarioSesion usuario) {
		if (usuario != null && "EMPLEADO".equals(usuario.getRol())
				&& usuario.getIdEmpleado() != null) {
			model.addAttribute("listapermisos",
					permisoService.listarPorEmpleado(usuario.getIdEmpleado()));
		} else {
			model.addAttribute("listapermisos", permisoService.listar(estado));
		}
		model.addAttribute("estado", estado);
		return "permisos/listar";
	}

	@GetMapping("/nuevo")
	public String nuevo(Model model, @AuthenticationPrincipal UsuarioSesion usuario) {
		PermisoRequestDto permiso = new PermisoRequestDto();
		boolean esEmpleado = usuario != null && "EMPLEADO".equals(usuario.getRol());
		if (esEmpleado) {
			permiso.setIdEmpleado(usuario.getIdEmpleado());
			model.addAttribute("nombreEmpleado", usuario.getNombreEmpleado());
		} else {
			model.addAttribute("listaempleados", empleadoService.listar(null));
		}
		model.addAttribute("esEmpleado", esEmpleado);
		model.addAttribute("permiso", permiso);
		return "permisos/crear";
	}

	@PostMapping("/guardar")
	public String guardar(@ModelAttribute PermisoRequestDto permiso, RedirectAttributes flash,
			@AuthenticationPrincipal UsuarioSesion usuario) {
		if (usuario != null && "EMPLEADO".equals(usuario.getRol())) {
			permiso.setIdEmpleado(usuario.getIdEmpleado());
		}
		return ejecutar(flash, "Permiso solicitado correctamente; queda PENDIENTE de RRHH",
				"redirect:/permisos/listar", () -> permisoService.crear(permiso));
	}

	@PostMapping("/{id}/aprobar")
	public String aprobar(@PathVariable Integer id, @RequestParam(required = false) String observacion,
			RedirectAttributes flash, @AuthenticationPrincipal UsuarioSesion usuario) {
		ResolucionPermisoRequestDto dto = new ResolucionPermisoRequestDto();
		dto.setIdUsuario(usuario.getIdUsuario());
		dto.setObservacion(observacion);
		return ejecutar(flash, "Permiso #" + id + " APROBADO",
				"redirect:/permisos/listar", () -> permisoService.aprobar(id, dto));
	}

	@PostMapping("/{id}/rechazar")
	public String rechazar(@PathVariable Integer id, @RequestParam(required = false) String observacion,
			RedirectAttributes flash, @AuthenticationPrincipal UsuarioSesion usuario) {
		ResolucionPermisoRequestDto dto = new ResolucionPermisoRequestDto();
		dto.setIdUsuario(usuario.getIdUsuario());
		dto.setObservacion(observacion);
		return ejecutar(flash, "Permiso #" + id + " RECHAZADO",
				"redirect:/permisos/listar", () -> permisoService.rechazar(id, dto));
	}
}
