package com.uisrael.empleadosweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uisrael.empleadosweb.model.dto.request.AreaRequestDto;
import com.uisrael.empleadosweb.model.dto.response.AreaResponseDto;
import com.uisrael.empleadosweb.services.IAreaService;

@Controller
@RequestMapping("/areas")
public class AreaController extends ControladorBase {

	private final IAreaService areaService;

	public AreaController(IAreaService areaService) {
		this.areaService = areaService;
	}

	@GetMapping
	public String listar(Model model) {
		model.addAttribute("listaareas", areaService.listar());
		return "areas/listar";
	}

	@GetMapping("/nuevo")
	public String nuevo(Model model) {
		model.addAttribute("area", new AreaRequestDto());
		model.addAttribute("titulo", "Crear Area");
		return "areas/crear";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable Integer id, Model model) {
		AreaResponseDto a = areaService.buscarPorId(id);
		AreaRequestDto dto = new AreaRequestDto();
		dto.setIdArea(a.getIdArea());
		dto.setNombre(a.getNombre());
		dto.setDescripcion(a.getDescripcion());
		dto.setEstado(a.isEstado());
		model.addAttribute("area", dto);
		model.addAttribute("titulo", "Editar Area");
		return "areas/crear";
	}

	@PostMapping("/guardar")
	public String guardar(@ModelAttribute AreaRequestDto area, RedirectAttributes flash) {
		try {
			if (area.getIdArea() == null) {
				areaService.guardar(area);
			} else {
				areaService.actualizar(area.getIdArea(), area);
			}
			flash.addFlashAttribute("mensaje", "Area guardada correctamente");
		} catch (WebClientResponseException ex) {
			flash.addFlashAttribute("error", mensajeError(ex));
		}
		return "redirect:/areas";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Integer id, RedirectAttributes flash) {
		try {
			areaService.eliminar(id);
			flash.addFlashAttribute("mensaje", "Area eliminada correctamente");
		} catch (WebClientResponseException ex) {
			flash.addFlashAttribute("error",
					"No se puede eliminar: el area tiene departamentos relacionados");
		}
		return "redirect:/areas";
	}
}
