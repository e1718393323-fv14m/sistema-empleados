package com.uisrael.empleadosweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uisrael.empleadosweb.model.dto.request.HorarioRequestDto;
import com.uisrael.empleadosweb.model.dto.response.HorarioResponseDto;
import com.uisrael.empleadosweb.services.IHorarioService;

@Controller
@RequestMapping("/horarios")
public class HorarioController extends ControladorBase {

	private final IHorarioService horarioService;

	public HorarioController(IHorarioService horarioService) {
		this.horarioService = horarioService;
	}

	@GetMapping
	public String listar(Model model) {
		model.addAttribute("listahorarios", horarioService.listar());
		return "horarios/listar";
	}

	@GetMapping("/nuevo")
	public String nuevo(Model model) {
		model.addAttribute("horario", new HorarioRequestDto());
		model.addAttribute("titulo", "Crear Horario");
		return "horarios/crear";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable Integer id, Model model) {
		HorarioResponseDto h = horarioService.buscarPorId(id);
		HorarioRequestDto dto = new HorarioRequestDto();
		dto.setIdHorario(h.getIdHorario());
		dto.setNombre(h.getNombre());
		dto.setHoraEntrada(h.getHoraEntrada());
		dto.setHoraSalida(h.getHoraSalida());
		dto.setToleranciaMinutos(h.getToleranciaMinutos());
		dto.setEstado(h.isEstado());
		model.addAttribute("horario", dto);
		model.addAttribute("titulo", "Editar Horario");
		return "horarios/crear";
	}

	@PostMapping("/guardar")
	public String guardar(@ModelAttribute HorarioRequestDto horario, RedirectAttributes flash) {
		try {
			if (horario.getIdHorario() == null) {
				horarioService.guardar(horario);
			} else {
				horarioService.actualizar(horario.getIdHorario(), horario);
			}
			flash.addFlashAttribute("mensaje", "Horario guardado correctamente");
		} catch (WebClientResponseException ex) {
			flash.addFlashAttribute("error", mensajeError(ex));
		}
		return "redirect:/horarios";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Integer id, RedirectAttributes flash) {
		try {
			horarioService.eliminar(id);
			flash.addFlashAttribute("mensaje", "Horario eliminado correctamente");
		} catch (WebClientResponseException ex) {
			flash.addFlashAttribute("error",
					"No se puede eliminar: el horario tiene empleados relacionados");
		}
		return "redirect:/horarios";
	}
}
