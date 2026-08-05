package com.uisrael.empleadosapi.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uisrael.empleadosapi.entities.Departamento;
import com.uisrael.empleadosapi.entities.Empleado;
import com.uisrael.empleadosapi.entities.Horario;
import com.uisrael.empleadosapi.exception.RecursoNoEncontradoException;
import com.uisrael.empleadosapi.exception.ReglaNegocioException;
import com.uisrael.empleadosapi.model.dto.request.EmpleadoRequestDto;
import com.uisrael.empleadosapi.model.dto.response.EmpleadoResponseDto;
import com.uisrael.empleadosapi.repository.IDepartamentoRepository;
import com.uisrael.empleadosapi.repository.IEmpleadoRepository;
import com.uisrael.empleadosapi.repository.IHorarioRepository;
import com.uisrael.empleadosapi.services.IEmpleadoService;

@Service
public class EmpleadoServiceImpl implements IEmpleadoService {

	private final IEmpleadoRepository empleadoRepository;
	private final IDepartamentoRepository departamentoRepository;
	private final IHorarioRepository horarioRepository;

	public EmpleadoServiceImpl(IEmpleadoRepository empleadoRepository,
			IDepartamentoRepository departamentoRepository,
			IHorarioRepository horarioRepository) {
		this.empleadoRepository = empleadoRepository;
		this.departamentoRepository = departamentoRepository;
		this.horarioRepository = horarioRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmpleadoResponseDto> listar() {
		return empleadoRepository.listarConRelaciones().stream().map(this::aDto).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmpleadoResponseDto> buscar(String texto) {
		return empleadoRepository.buscarPorNombreOApellido(texto).stream().map(this::aDto).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public EmpleadoResponseDto buscarPorCodigo(Integer codigo) {
		return aDto(obtener(codigo));
	}

	@Override
	@Transactional
	public EmpleadoResponseDto guardar(EmpleadoRequestDto dto) {
		// Regla de negocio: la cedula no debe repetirse
		empleadoRepository.buscarPorCedula(dto.getCedula()).ifPresent(e -> {
			throw new ReglaNegocioException("Ya existe un empleado con la cedula " + dto.getCedula());
		});
		Empleado e = new Empleado();
		copiar(dto, e);
		return aDto(empleadoRepository.save(e));
	}

	@Override
	@Transactional
	public EmpleadoResponseDto actualizar(Integer codigo, EmpleadoRequestDto dto) {
		Empleado e = obtener(codigo);
		// si cambia la cedula, validar que no pertenezca a otro empleado
		empleadoRepository.buscarPorCedula(dto.getCedula())
				.filter(otro -> !otro.getIdEmpleado().equals(codigo))
				.ifPresent(otro -> {
					throw new ReglaNegocioException("La cedula ya pertenece a otro empleado");
				});
		copiar(dto, e);
		return aDto(empleadoRepository.save(e));
	}

	@Override
	@Transactional
	public void eliminar(Integer codigo) {
		empleadoRepository.delete(obtener(codigo));
	}

	private Empleado obtener(Integer codigo) {
		return empleadoRepository.buscarPorCodigo(codigo)
				.orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado: " + codigo));
	}

	private void copiar(EmpleadoRequestDto dto, Empleado e) {
		e.setCodigoAlterno(dto.getCodigoAlterno());
		e.setNombres(dto.getNombres());
		e.setApellidos(dto.getApellidos());
		e.setCedula(dto.getCedula());
		e.setEmail(dto.getEmail());
		e.setTelefono(dto.getTelefono());
		e.setFechaNacimiento(dto.getFechaNacimiento());
		e.setEstadoCivil(dto.getEstadoCivil());
		e.setSexo(dto.getSexo());
		e.setNivelEstudios(dto.getNivelEstudios());
		e.setCiudad(dto.getCiudad());
		e.setDireccion(dto.getDireccion());
		e.setPuesto(dto.getPuesto());
		e.setFechaIngreso(dto.getFechaIngreso());
		e.setEstado(dto.isEstado());

		Departamento d = departamentoRepository.findById(dto.getIdDepartamento())
				.orElseThrow(() -> new RecursoNoEncontradoException(
						"Departamento no encontrado: " + dto.getIdDepartamento()));
		e.setDepartamento(d);

		Horario h = horarioRepository.findById(dto.getIdHorario())
				.orElseThrow(() -> new RecursoNoEncontradoException(
						"Horario no encontrado: " + dto.getIdHorario()));
		e.setHorario(h);
	}

	private EmpleadoResponseDto aDto(Empleado e) {
		EmpleadoResponseDto r = new EmpleadoResponseDto();
		r.setIdEmpleado(e.getIdEmpleado());
		r.setCodigoAlterno(e.getCodigoAlterno());
		r.setNombres(e.getNombres());
		r.setApellidos(e.getApellidos());
		r.setCedula(e.getCedula());
		r.setEmail(e.getEmail());
		r.setTelefono(e.getTelefono());
		r.setFechaNacimiento(e.getFechaNacimiento());
		r.setEstadoCivil(e.getEstadoCivil());
		r.setSexo(e.getSexo());
		r.setNivelEstudios(e.getNivelEstudios());
		r.setCiudad(e.getCiudad());
		r.setDireccion(e.getDireccion());
		r.setPuesto(e.getPuesto());
		r.setFechaIngreso(e.getFechaIngreso());
		r.setEstado(e.isEstado());
		r.setIdDepartamento(e.getDepartamento().getIdDepartamento());
		r.setNombreDepartamento(e.getDepartamento().getNombre());
		r.setNombreArea(e.getDepartamento().getArea().getNombre());
		r.setIdHorario(e.getHorario().getIdHorario());
		r.setNombreHorario(e.getHorario().getNombre());
		return r;
	}
}
