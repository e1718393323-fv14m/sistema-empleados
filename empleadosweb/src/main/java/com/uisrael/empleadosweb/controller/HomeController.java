package com.uisrael.empleadosweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.uisrael.empleadosweb.services.IAsistenciaService;
import com.uisrael.empleadosweb.services.IEmpleadoService;

@Controller
public class HomeController {

	private final IEmpleadoService empleadoService;
	private final IAsistenciaService asistenciaService;

	public HomeController(IEmpleadoService empleadoService, IAsistenciaService asistenciaService) {
		this.empleadoService = empleadoService;
		this.asistenciaService = asistenciaService;
	}

	@GetMapping("/")
	public String inicio(Model model) {
		model.addAttribute("totalEmpleados", empleadoService.listar(null).size());
		model.addAttribute("asistencias", asistenciaService.listar());
		return "index";
	}

	// Pagina amigable cuando el rol no tiene permisos (ver SeguridadConfig)
	@GetMapping("/acceso-denegado")
	public String accesoDenegado() {
		return "acceso-denegado";
	}
}
