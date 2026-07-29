package com.uisrael.empleadosapi.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uisrael.empleadosapi.entities.Empleado;
import com.uisrael.empleadosapi.entities.Permiso;
import com.uisrael.empleadosapi.entities.Usuario;
import com.uisrael.empleadosapi.exception.RecursoNoEncontradoException;
import com.uisrael.empleadosapi.exception.ReglaNegocioException;
import com.uisrael.empleadosapi.model.dto.request.PermisoRequestDto;
import com.uisrael.empleadosapi.model.dto.request.ResolucionPermisoRequestDto;
import com.uisrael.empleadosapi.model.dto.response.PermisoResponseDto;
import com.uisrael.empleadosapi.repository.IEmpleadoRepository;
import com.uisrael.empleadosapi.repository.IPermisoRepository;
import com.uisrael.empleadosapi.repository.IUsuarioRepository;
import com.uisrael.empleadosapi.services.IPermisoService;

/**
 * Flujo de permisos por ausencia:
 * PENDIENTE -> (solo RRHH o ADMIN) -> APROBADO | RECHAZADO
 */
@Service
public class PermisoServiceImpl implements IPermisoService {

	private final IPermisoRepository permisoRepository;
	private final IEmpleadoRepository empleadoRepository;
	private final IUsuarioRepository usuarioRepository;

	public PermisoServiceImpl(IPermisoRepository permisoRepository,
			IEmpleadoRepository empleadoRepository, IUsuarioRepository usuarioRepository) {
		this.permisoRepository = permisoRepository;
		this.empleadoRepository = empleadoRepository;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PermisoResponseDto> listar(String estado, Integer idEmpleado) {
		List<Permiso> lista;
		if (idEmpleado != null) {
			lista = permisoRepository.listarPorEmpleado(idEmpleado);
		} else if (estado != null && !estado.isBlank()) {
			lista = permisoRepository.listarPorEstado(estado.toUpperCase());
		} else {
			lista = permisoRepository.listarConEmpleado();
		}
		return lista.stream().map(this::aDto).toList();
	}

	@Override
	@Transactional
	public PermisoResponseDto crear(PermisoRequestDto dto) {
		Empleado empleado = empleadoRepository.findById(dto.getIdEmpleado())
				.orElseThrow(() -> new RecursoNoEncontradoException(
						"No existe el empleado con codigo " + dto.getIdEmpleado()));
		if (dto.getFechaDesde() == null || dto.getFechaHasta() == null
				|| dto.getFechaHasta().isBefore(dto.getFechaDesde())) {
			throw new ReglaNegocioException("El rango de fechas del permiso no es valido");
		}
		if (dto.getMotivo() == null || dto.getMotivo().isBlank()) {
			throw new ReglaNegocioException("El motivo del permiso es obligatorio");
		}
		Permiso p = new Permiso();
		p.setEmpleado(empleado);
		p.setFechaSolicitud(LocalDateTime.now().withNano(0));
		p.setFechaDesde(dto.getFechaDesde());
		p.setFechaHasta(dto.getFechaHasta());
		p.setTipoPermiso(dto.getTipoPermiso() != null ? dto.getTipoPermiso() : "OTRO");
		p.setMotivo(dto.getMotivo());
		p.setEstado("PENDIENTE");
		return aDto(permisoRepository.save(p));
	}

	@Override
	@Transactional
	public PermisoResponseDto aprobar(Integer idPermiso, ResolucionPermisoRequestDto dto) {
		return resolver(idPermiso, dto, "APROBADO");
	}

	@Override
	@Transactional
	public PermisoResponseDto rechazar(Integer idPermiso, ResolucionPermisoRequestDto dto) {
		return resolver(idPermiso, dto, "RECHAZADO");
	}

	private PermisoResponseDto resolver(Integer idPermiso, ResolucionPermisoRequestDto dto,
			String nuevoEstado) {
		Permiso p = permisoRepository.findById(idPermiso)
				.orElseThrow(() -> new RecursoNoEncontradoException(
						"No existe el permiso " + idPermiso));
		if (!"PENDIENTE".equals(p.getEstado())) {
			throw new ReglaNegocioException(
					"El permiso ya fue resuelto (" + p.getEstado() + ")");
		}
		Usuario aprobador = usuarioRepository.findById(dto.getIdUsuario())
				.orElseThrow(() -> new RecursoNoEncontradoException(
						"No existe el usuario " + dto.getIdUsuario()));
		String rol = aprobador.getRol().getNombre();
		if (!"RRHH".equals(rol) && !"ADMIN".equals(rol)) {
			throw new ReglaNegocioException(
					"Solo el rol RRHH puede aprobar o rechazar permisos");
		}
		p.setEstado(nuevoEstado);
		p.setAprobadoPor(aprobador);
		p.setFechaResolucion(LocalDateTime.now().withNano(0));
		p.setObservacionRrhh(dto.getObservacion());
		return aDto(permisoRepository.save(p));
	}

	private PermisoResponseDto aDto(Permiso p) {
		PermisoResponseDto r = new PermisoResponseDto();
		r.setIdPermiso(p.getIdPermiso());
		r.setIdEmpleado(p.getEmpleado().getIdEmpleado());
		r.setNombreEmpleado(p.getEmpleado().getApellidos() + " " + p.getEmpleado().getNombres());
		r.setFechaSolicitud(p.getFechaSolicitud());
		r.setFechaDesde(p.getFechaDesde());
		r.setFechaHasta(p.getFechaHasta());
		r.setTipoPermiso(p.getTipoPermiso());
		r.setMotivo(p.getMotivo());
		r.setEstado(p.getEstado());
		r.setAprobadoPor(p.getAprobadoPor() != null ? p.getAprobadoPor().getUsername() : null);
		r.setFechaResolucion(p.getFechaResolucion());
		r.setObservacionRrhh(p.getObservacionRrhh());
		return r;
	}
}
