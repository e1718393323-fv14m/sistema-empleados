package com.uisrael.empleadosapi.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uisrael.empleadosapi.entities.Ingreso;
import com.uisrael.empleadosapi.entities.Usuario;
import com.uisrael.empleadosapi.exception.RecursoNoEncontradoException;
import com.uisrael.empleadosapi.exception.ReglaNegocioException;
import com.uisrael.empleadosapi.model.dto.request.LoginRequestDto;
import com.uisrael.empleadosapi.model.dto.request.UsuarioRequestDto;
import com.uisrael.empleadosapi.model.dto.response.IngresoResponseDto;
import com.uisrael.empleadosapi.model.dto.response.UsuarioResponseDto;
import com.uisrael.empleadosapi.repository.IEmpleadoRepository;
import com.uisrael.empleadosapi.repository.IIngresoRepository;
import com.uisrael.empleadosapi.repository.IRolRepository;
import com.uisrael.empleadosapi.repository.IUsuarioRepository;
import com.uisrael.empleadosapi.services.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

	private final IUsuarioRepository usuarioRepository;
	private final IRolRepository rolRepository;
	private final IEmpleadoRepository empleadoRepository;
	private final IIngresoRepository ingresoRepository;
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public UsuarioServiceImpl(IUsuarioRepository usuarioRepository, IRolRepository rolRepository,
			IEmpleadoRepository empleadoRepository, IIngresoRepository ingresoRepository) {
		this.usuarioRepository = usuarioRepository;
		this.rolRepository = rolRepository;
		this.empleadoRepository = empleadoRepository;
		this.ingresoRepository = ingresoRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UsuarioResponseDto> listar() {
		return usuarioRepository.listarConRelaciones().stream().map(this::aDto).toList();
	}

	@Override
	@Transactional
	public UsuarioResponseDto crear(UsuarioRequestDto dto) {
		if (dto.getUsername() == null || dto.getUsername().isBlank()
				|| dto.getPassword() == null || dto.getPassword().isBlank()) {
			throw new ReglaNegocioException("Usuario y contrasena son obligatorios");
		}
		usuarioRepository.buscarPorUsername(dto.getUsername()).ifPresent(u -> {
			throw new ReglaNegocioException("Ya existe un usuario con ese nombre");
		});
		Usuario u = new Usuario();
		u.setUsername(dto.getUsername().trim());
		u.setPassword(encoder.encode(dto.getPassword()));
		u.setRol(rolRepository.findById(dto.getIdRol())
				.orElseThrow(() -> new RecursoNoEncontradoException("No existe el rol")));
		if (dto.getIdEmpleado() != null) {
			u.setEmpleado(empleadoRepository.findById(dto.getIdEmpleado())
					.orElseThrow(() -> new RecursoNoEncontradoException("No existe el empleado")));
		}
		u.setEstado(dto.getEstado() == null || dto.getEstado());
		return aDto(usuarioRepository.save(u));
	}

	@Override
	@Transactional
	public UsuarioResponseDto autenticar(LoginRequestDto dto) {
		Usuario u = usuarioRepository.buscarPorUsername(dto.getUsername()).orElse(null);
		boolean ok = u != null && u.isEstado()
				&& encoder.matches(dto.getPassword(), u.getPassword());

		// Auditoria: registrar SIEMPRE el intento (exitoso o no)
		Ingreso i = new Ingreso();
		i.setUsuario(u);
		i.setUsernameIntentado(dto.getUsername());
		i.setFechaHora(LocalDateTime.now().withNano(0));
		i.setIp(dto.getIp());
		i.setExitoso(ok);
		ingresoRepository.save(i);

		if (!ok) {
			throw new ReglaNegocioException("Usuario o contrasena incorrectos");
		}
		u.setUltimoIngreso(LocalDateTime.now().withNano(0));
		usuarioRepository.save(u);
		return aDto(u);
	}

	@Override
	@Transactional(readOnly = true)
	public List<IngresoResponseDto> listarIngresos() {
		return ingresoRepository.listarUltimos().stream().map(i -> {
			IngresoResponseDto r = new IngresoResponseDto();
			r.setIdIngreso(i.getIdIngreso());
			r.setUsername(i.getUsuario() != null ? i.getUsuario().getUsername()
					: i.getUsernameIntentado() + " (no existe)");
			r.setFechaHora(i.getFechaHora());
			r.setIp(i.getIp());
			r.setExitoso(i.isExitoso());
			return r;
		}).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public UsuarioResponseDto buscarPorId(Integer idUsuario) {
		Usuario u = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new RecursoNoEncontradoException(
						"No existe el usuario " + idUsuario));
		return aDto(u);
	}

	@Override
	@Transactional
	public UsuarioResponseDto actualizar(Integer idUsuario, UsuarioRequestDto dto) {
		Usuario u = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new RecursoNoEncontradoException(
						"No existe el usuario " + idUsuario));
		if (dto.getUsername() == null || dto.getUsername().isBlank()) {
			throw new ReglaNegocioException("El nombre de usuario es obligatorio");
		}
		// Username unico (excluyendo al propio usuario)
		usuarioRepository.buscarPorUsername(dto.getUsername().trim()).ifPresent(otro -> {
			if (!otro.getIdUsuario().equals(idUsuario)) {
				throw new ReglaNegocioException("Ya existe otro usuario con ese nombre");
			}
		});
		u.setUsername(dto.getUsername().trim());
		// Password opcional: en blanco = conservar la actual
		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			u.setPassword(encoder.encode(dto.getPassword()));
		}
		if (dto.getIdRol() != null) {
			u.setRol(rolRepository.findById(dto.getIdRol())
					.orElseThrow(() -> new RecursoNoEncontradoException("No existe el rol")));
		}
		u.setEmpleado(dto.getIdEmpleado() != null
				? empleadoRepository.findById(dto.getIdEmpleado())
						.orElseThrow(() -> new RecursoNoEncontradoException("No existe el empleado"))
				: null);
		if (dto.getEstado() != null) {
			u.setEstado(dto.getEstado());
		}
		validarUltimoAdminActivo(u);
		return aDto(usuarioRepository.save(u));
	}

	@Override
	@Transactional
	public void eliminar(Integer idUsuario) {
		Usuario u = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new RecursoNoEncontradoException(
						"No existe el usuario " + idUsuario));
		if ("ADMIN".equals(u.getRol().getNombre()) && contarAdminsActivos() <= 1) {
			throw new ReglaNegocioException(
					"No se puede eliminar el ultimo usuario ADMIN activo del sistema");
		}
		try {
			usuarioRepository.delete(u);
			usuarioRepository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new ReglaNegocioException("El usuario tiene registros asociados "
					+ "(ingresos o permisos resueltos); desactivelo en lugar de eliminarlo");
		}
	}

	// Evita dejar al sistema sin administradores al desactivar/cambiar rol
	private void validarUltimoAdminActivo(Usuario editado) {
		boolean sigueSiendoAdminActivo = editado.isEstado()
				&& "ADMIN".equals(editado.getRol().getNombre());
		if (!sigueSiendoAdminActivo && contarAdminsActivos(editado.getIdUsuario()) == 0) {
			throw new ReglaNegocioException(
					"Debe existir al menos un usuario ADMIN activo en el sistema");
		}
	}

	private long contarAdminsActivos() {
		return contarAdminsActivos(null);
	}

	private long contarAdminsActivos(Integer excluirId) {
		return usuarioRepository.listarConRelaciones().stream()
				.filter(x -> x.isEstado() && "ADMIN".equals(x.getRol().getNombre()))
				.filter(x -> excluirId == null || !x.getIdUsuario().equals(excluirId))
				.count();
	}

	private UsuarioResponseDto aDto(Usuario u) {
		UsuarioResponseDto r = new UsuarioResponseDto();
		r.setIdUsuario(u.getIdUsuario());
		r.setUsername(u.getUsername());
		r.setRol(u.getRol().getNombre());
		if (u.getEmpleado() != null) {
			r.setIdEmpleado(u.getEmpleado().getIdEmpleado());
			r.setNombreEmpleado(u.getEmpleado().getApellidos() + " " + u.getEmpleado().getNombres());
		}
		r.setEstado(u.isEstado());
		r.setUltimoIngreso(u.getUltimoIngreso());
		return r;
	}
}
