package com.uisrael.empleadosweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uisrael.empleadosweb.model.dto.request.EmpleadoRequestDto;
import com.uisrael.empleadosweb.model.dto.response.EmpleadoResponseDto;
import com.uisrael.empleadosweb.services.IDepartamentoService;
import com.uisrael.empleadosweb.services.IEmpleadoService;
import com.uisrael.empleadosweb.services.IHorarioService;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController extends ControladorBase {

	private final IEmpleadoService empleadoService;
	private final IDepartamentoService departamentoService;
	private final IHorarioService horarioService;

	public EmpleadoController(IEmpleadoService empleadoService,
			IDepartamentoService departamentoService, IHorarioService horarioService) {
		this.empleadoService = empleadoService;
		this.departamentoService = departamentoService;
		this.horarioService = horarioService;
	}

	// LISTAR (R del CRUD) con busqueda opcional
	@GetMapping
	public String listar(@RequestParam(required = false) String buscar, Model model) {
		model.addAttribute("listaempleados", empleadoService.listar(buscar));
		model.addAttribute("buscar", buscar);
		return "empleados/listar";
	}

	// FORMULARIO NUEVO (C del CRUD)
	@GetMapping("/nuevo")
	public String nuevo(Model model) {
		model.addAttribute("empleado", new EmpleadoRequestDto());
		cargarCatalogos(model);
		model.addAttribute("titulo", "Registrar Empleado");
		return "empleados/crear";
	}

	// FORMULARIO EDITAR (U del CRUD)
	@GetMapping("/editar/{codigo}")
	public String editar(@PathVariable Integer codigo, Model model) {
		EmpleadoResponseDto e = empleadoService.buscarPorCodigo(codigo);
		EmpleadoRequestDto dto = new EmpleadoRequestDto();
		dto.setIdEmpleado(e.getIdEmpleado());
		dto.setCodigoAlterno(e.getCodigoAlterno());
		dto.setNombres(e.getNombres());
		dto.setApellidos(e.getApellidos());
		dto.setCedula(e.getCedula());
		dto.setEmail(e.getEmail());
		dto.setTelefono(e.getTelefono());
		dto.setFechaNacimiento(e.getFechaNacimiento());
		dto.setEstadoCivil(e.getEstadoCivil());
		dto.setSexo(e.getSexo());
		dto.setNivelEstudios(e.getNivelEstudios());
		dto.setCiudad(e.getCiudad());
		dto.setDireccion(e.getDireccion());
		dto.setPuesto(e.getPuesto());
		dto.setFechaIngreso(e.getFechaIngreso());
		dto.setEstado(e.isEstado());
		dto.setIdDepartamento(e.getIdDepartamento());
		dto.setIdHorario(e.getIdHorario());
		model.addAttribute("empleado", dto);
		cargarCatalogos(model);
		model.addAttribute("titulo", "Editar Empleado - Codigo " + codigo);
		return "empleados/crear";
	}

	// GUARDAR (crea o actualiza segun exista el codigo)
	@PostMapping("/guardar")
	public String guardar(@ModelAttribute EmpleadoRequestDto empleado, RedirectAttributes flash) {
		try {
			if (empleado.getIdEmpleado() == null) {
				empleadoService.guardar(empleado);
				flash.addFlashAttribute("mensaje", "Empleado registrado correctamente");
			} else {
				empleadoService.actualizar(empleado.getIdEmpleado(), empleado);
				flash.addFlashAttribute("mensaje", "Empleado actualizado correctamente");
			}
		} catch (WebClientResponseException ex) {
			flash.addFlashAttribute("error", mensajeError(ex));
		}
		return "redirect:/empleados";
	}

	// ELIMINAR (D del CRUD)
	@GetMapping("/eliminar/{codigo}")
	public String eliminar(@PathVariable Integer codigo, RedirectAttributes flash) {
		try {
			empleadoService.eliminar(codigo);
			flash.addFlashAttribute("mensaje", "Empleado eliminado correctamente");
		} catch (WebClientResponseException ex) {
			flash.addFlashAttribute("error",
					"No se puede eliminar: el empleado tiene registros relacionados");
		}
		return "redirect:/empleados";
	}

	private void cargarCatalogos(Model model) {
		model.addAttribute("departamentos", departamentoService.listar());
		model.addAttribute("horarios", horarioService.listar());
	}
}
