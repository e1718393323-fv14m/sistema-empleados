package com.uisrael.empleadosweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uisrael.empleadosweb.model.dto.request.AsistenciaRequestDto;
import com.uisrael.empleadosweb.model.dto.response.ProcesoMarcacionResponseDto;
import com.uisrael.empleadosweb.services.IAsistenciaService;
import com.uisrael.empleadosweb.services.IPermisoService;
import com.uisrael.empleadosweb.services.IEmpleadoService;
import com.uisrael.empleadosweb.services.IMarcacionService;

@Controller
@RequestMapping("/asistencias")
public class AsistenciaController extends ControladorBase {

	private final IAsistenciaService asistenciaService;
	private final IPermisoService permisoService;
	private final IEmpleadoService empleadoService;
	private final IMarcacionService marcacionService;

	public AsistenciaController(IAsistenciaService asistenciaService,
			IEmpleadoService empleadoService, IMarcacionService marcacionService,
			IPermisoService permisoService) {
		this.asistenciaService = asistenciaService;
		this.empleadoService = empleadoService;
		this.marcacionService = marcacionService;
			this.permisoService = permisoService;
	}

	@GetMapping
	public String listar(@RequestParam(required = false) Integer codigo, Model model) {
		if (codigo != null) {
			model.addAttribute("listaasistencias", asistenciaService.listarPorEmpleado(codigo));
			model.addAttribute("listapermisos", permisoService.listarPorEmpleado(codigo)
					.stream().filter(pp -> "APROBADO".equals(pp.getEstado())).toList());
		} else {
			model.addAttribute("listaasistencias", asistenciaService.listar());
		}
		model.addAttribute("codigo", codigo);
		return "asistencias/listar";
	}

	@GetMapping("/registrar")
	public String registrar(Model model) {
		model.addAttribute("asistencia", new AsistenciaRequestDto());
		model.addAttribute("empleados", empleadoService.listar(null));
		return "asistencias/registrar";
	}

	@PostMapping("/entrada")
	public String marcarEntrada(@ModelAttribute AsistenciaRequestDto asistencia,
			RedirectAttributes flash) {
		try {
			asistenciaService.marcarEntrada(asistencia);
			flash.addFlashAttribute("mensaje", "Entrada registrada correctamente");
		} catch (WebClientResponseException ex) {
			flash.addFlashAttribute("error", mensajeError(ex));
		}
		return "redirect:/asistencias";
	}

	@PostMapping("/salida")
	public String marcarSalida(@ModelAttribute AsistenciaRequestDto asistencia,
			RedirectAttributes flash) {
		try {
			asistenciaService.marcarSalida(asistencia);
			flash.addFlashAttribute("mensaje", "Salida registrada correctamente");
		} catch (WebClientResponseException ex) {
			flash.addFlashAttribute("error", mensajeError(ex));
		}
		return "redirect:/asistencias";
	}

	@PostMapping("/procesar")
	public String procesarMarcaciones(RedirectAttributes flash) {
		try {
			ProcesoMarcacionResponseDto r = marcacionService.procesar();
			String msg = "Proceso ejecutado: " + r.getMarcacionesLeidas() + " marcaciones leidas, "
					+ r.getAsistenciasCreadas() + " asistencias creadas, "
					+ r.getAsistenciasActualizadas() + " actualizadas";
			if (r.getSinEmpleado() > 0) {
				msg += ". Sin match: codigos " + String.join(", ", r.getCodigosSinMatch());
			}
			flash.addFlashAttribute("mensaje", msg);
		} catch (WebClientResponseException ex) {
			flash.addFlashAttribute("error", mensajeError(ex));
		}
		return "redirect:/asistencias";
	}
}
