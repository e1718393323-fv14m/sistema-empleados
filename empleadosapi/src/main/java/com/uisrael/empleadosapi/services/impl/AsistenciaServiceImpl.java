package com.uisrael.empleadosapi.services.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uisrael.empleadosapi.entities.Asistencia;
import com.uisrael.empleadosapi.entities.Empleado;
import com.uisrael.empleadosapi.entities.Horario;
import com.uisrael.empleadosapi.exception.RecursoNoEncontradoException;
import com.uisrael.empleadosapi.exception.ReglaNegocioException;
import com.uisrael.empleadosapi.model.dto.request.AsistenciaRequestDto;
import com.uisrael.empleadosapi.model.dto.response.AsistenciaResponseDto;
import com.uisrael.empleadosapi.repository.IAsistenciaRepository;
import com.uisrael.empleadosapi.entities.Permiso;
import com.uisrael.empleadosapi.repository.IEmpleadoRepository;
import com.uisrael.empleadosapi.repository.IPermisoRepository;
import com.uisrael.empleadosapi.services.IAsistenciaService;

@Service
public class AsistenciaServiceImpl implements IAsistenciaService {

	private final IAsistenciaRepository asistenciaRepository;
	private final IEmpleadoRepository empleadoRepository;
	private final IPermisoRepository permisoRepository;

	public AsistenciaServiceImpl(IAsistenciaRepository asistenciaRepository,
			IEmpleadoRepository empleadoRepository, IPermisoRepository permisoRepository) {
		this.asistenciaRepository = asistenciaRepository;
		this.empleadoRepository = empleadoRepository;
		this.permisoRepository = permisoRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<AsistenciaResponseDto> listar() {
		return asistenciaRepository.listarConEmpleado().stream().map(this::aDto).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<AsistenciaResponseDto> listarPorEmpleado(Integer codigo) {
		return asistenciaRepository.listarPorEmpleado(codigo).stream().map(this::aDto).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<AsistenciaResponseDto> listarPorRango(LocalDate desde, LocalDate hasta) {
		return asistenciaRepository.listarPorRango(desde, hasta).stream().map(this::aDto).toList();
	}

	@Override
	@Transactional
	public AsistenciaResponseDto marcarEntrada(AsistenciaRequestDto dto) {
		Empleado empleado = empleadoRepository.buscarPorCodigo(dto.getIdEmpleado())
				.orElseThrow(() -> new RecursoNoEncontradoException(
						"Empleado no encontrado: " + dto.getIdEmpleado()));

		LocalDate hoy = LocalDate.now();
		asistenciaRepository.buscarPorEmpleadoYFecha(empleado.getIdEmpleado(), hoy).ifPresent(a -> {
			throw new ReglaNegocioException("El empleado ya registro su entrada el dia de hoy");
		});

		LocalTime ahora = LocalTime.now().withNano(0);

		// Comparar contra el horario asignado (08:30 + tolerancia)
		Horario horario = empleado.getHorario();
		int tolerancia = horario.getToleranciaMinutos() != null ? horario.getToleranciaMinutos() : 0;
		LocalTime limite = horario.getHoraEntrada().plusMinutes(tolerancia);

		Asistencia a = new Asistencia();
		a.setEmpleado(empleado);
		a.setCodigoAlterno(empleado.getCodigoAlterno()); // registra el codigo alterno
		a.setFecha(hoy);
		a.setHoraEntrada(ahora);
		a.setEstadoMarcacion(ahora.isAfter(limite) ? "ATRASO" : "PUNTUAL");
		a.setObservacion(dto.getObservacion());
		return aDto(asistenciaRepository.save(a));
	}

	@Override
	@Transactional
	public AsistenciaResponseDto marcarSalida(AsistenciaRequestDto dto) {
		LocalDate hoy = LocalDate.now();
		Asistencia a = asistenciaRepository
				.buscarPorEmpleadoYFecha(dto.getIdEmpleado(), hoy)
				.orElseThrow(() -> new ReglaNegocioException(
						"El empleado no tiene registro de entrada el dia de hoy"));

		if (a.getHoraSalida() != null) {
			throw new ReglaNegocioException("El empleado ya registro su salida el dia de hoy");
		}

		// REGLA DE NEGOCIO: la salida solo puede registrarse desde la hora de salida
		// del horario del empleado (ej. 17:00). Excepcion: permiso APROBADO vigente.
		LocalTime ahora = LocalTime.now().withNano(0);
		LocalTime horaSalidaHorario = a.getEmpleado().getHorario().getHoraSalida();
		String notaPermiso = null;
		if (ahora.isBefore(horaSalidaHorario)) {
			Permiso permiso = permisoRepository
					.buscarAprobadoVigente(a.getEmpleado().getIdEmpleado(), hoy)
					.orElseThrow(() -> new ReglaNegocioException(
							"La salida solo puede registrarse a partir de las "
							+ horaSalidaHorario));
			notaPermiso = "Salida anticipada por permiso #" + permiso.getIdPermiso();
		}
		a.setHoraSalida(ahora);
		if (dto.getObservacion() != null && !dto.getObservacion().isBlank()) {
			a.setObservacion(dto.getObservacion());
		}
		if (notaPermiso != null) {
			a.setObservacion((a.getObservacion() != null ? a.getObservacion() + " | " : "")
					+ notaPermiso);
		}
		return aDto(asistenciaRepository.save(a));
	}

	private AsistenciaResponseDto aDto(Asistencia a) {
		AsistenciaResponseDto r = new AsistenciaResponseDto();
		r.setIdAsistencia(a.getIdAsistencia());
		r.setFecha(a.getFecha());
		r.setHoraEntrada(a.getHoraEntrada());
		r.setHoraSalida(a.getHoraSalida());
		r.setEstadoMarcacion(a.getEstadoMarcacion());
		r.setObservacion(a.getObservacion());
		r.setIdEmpleado(a.getEmpleado().getIdEmpleado());
		// Si el registro es antiguo y no guardo el codigo alterno, se toma del empleado
		r.setCodigoAlterno(a.getCodigoAlterno() != null
				? a.getCodigoAlterno()
				: a.getEmpleado().getCodigoAlterno());
		r.setNombreEmpleado(a.getEmpleado().getApellidos() + " " + a.getEmpleado().getNombres());
		return r;
	}
}
