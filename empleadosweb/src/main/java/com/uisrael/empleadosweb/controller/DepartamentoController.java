package com.uisrael.empleadosweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uisrael.empleadosweb.model.dto.request.DepartamentoRequestDto;
import com.uisrael.empleadosweb.model.dto.response.DepartamentoResponseDto;
import com.uisrael.empleadosweb.services.IAreaService;
import com.uisrael.empleadosweb.services.IDepartamentoService;

@Controller
@RequestMapping("/departamentos")
public class DepartamentoController extends ControladorBase {

	private final IDepartamentoService departamentoService;
	private final IAreaService areaService;

	public DepartamentoController(IDepartamentoService departamentoService,
			IAreaService areaService) {
		this.departamentoService = departamentoService;
		this.areaService = areaService;
	}

	@GetMapping
	public String listar(Model model) {
		model.addAttribute("listadepartamentos", departamentoService.listar());
		return "departamentos/listar";
	}

	@GetMapping("/nuevo")
	public String nuevo(Model model) {
		model.addAttribute("departamento", new DepartamentoRequestDto());
		model.addAttribute("areas", areaService.listar());
		model.addAttribute("titulo", "Crear Departamento");
		return "departamentos/crear";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable Integer id, Model model) {
		DepartamentoResponseDto d = departamentoService.buscarPorId(id);
		DepartamentoRequestDto dto = new DepartamentoRequestDto();
		dto.setIdDepartamento(d.getIdDepartamento());
		dto.setNombre(d.getNombre());
		dto.setDescripcion(d.getDescripcion());
		dto.setEstado(d.isEstado());
		dto.setIdArea(d.getIdArea());
		model.addAttribute("departamento", dto);
		model.addAttribute("areas", areaService.listar());
		model.addAttribute("titulo", "Editar Departamento");
		return "departamentos/crear";
	}

	@PostMapping("/guardar")
	public String guardar(@ModelAttribute DepartamentoRequestDto departamento,
			RedirectAttributes flash) {
		try {
			if (departamento.getIdDepartamento() == null) {
				departamentoService.guardar(departamento);
			} else {
				departamentoService.actualizar(departamento.getIdDepartamento(), departamento);
			}
			flash.addFlashAttribute("mensaje", "Departamento guardado correctamente");
		} catch (WebClientResponseException ex) {
			flash.addFlashAttribute("error", mensajeError(ex));
		}
		return "redirect:/departamentos";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Integer id, RedirectAttributes flash) {
		try {
			departamentoService.eliminar(id);
			flash.addFlashAttribute("mensaje", "Departamento eliminado correctamente");
		} catch (WebClientResponseException ex) {
			flash.addFlashAttribute("error",
					"No se puede eliminar: el departamento tiene empleados relacionados");
		}
		return "redirect:/departamentos";
	}
}
