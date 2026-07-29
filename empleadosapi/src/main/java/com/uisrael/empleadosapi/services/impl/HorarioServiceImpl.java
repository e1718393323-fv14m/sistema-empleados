package com.uisrael.empleadosapi.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uisrael.empleadosapi.entities.Horario;
import com.uisrael.empleadosapi.exception.RecursoNoEncontradoException;
import com.uisrael.empleadosapi.model.dto.request.HorarioRequestDto;
import com.uisrael.empleadosapi.model.dto.response.HorarioResponseDto;
import com.uisrael.empleadosapi.repository.IHorarioRepository;
import com.uisrael.empleadosapi.services.IHorarioService;

@Service
public class HorarioServiceImpl implements IHorarioService {

	private final IHorarioRepository horarioRepository;

	public HorarioServiceImpl(IHorarioRepository horarioRepository) {
		this.horarioRepository = horarioRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<HorarioResponseDto> listar() {
		return horarioRepository.findAll().stream().map(this::aDto).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public HorarioResponseDto buscarPorId(Integer id) {
		return aDto(obtener(id));
	}

	@Override
	@Transactional
	public HorarioResponseDto guardar(HorarioRequestDto dto) {
		Horario h = new Horario();
		copiar(dto, h);
		return aDto(horarioRepository.save(h));
	}

	@Override
	@Transactional
	public HorarioResponseDto actualizar(Integer id, HorarioRequestDto dto) {
		Horario h = obtener(id);
		copiar(dto, h);
		return aDto(horarioRepository.save(h));
	}

	@Override
	@Transactional
	public void eliminar(Integer id) {
		horarioRepository.delete(obtener(id));
	}

	private Horario obtener(Integer id) {
		return horarioRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Horario no encontrado: " + id));
	}

	private void copiar(HorarioRequestDto dto, Horario h) {
		h.setNombre(dto.getNombre());
		h.setHoraEntrada(dto.getHoraEntrada());
		h.setHoraSalida(dto.getHoraSalida());
		h.setToleranciaMinutos(dto.getToleranciaMinutos());
		h.setEstado(dto.isEstado());
	}

	private HorarioResponseDto aDto(Horario h) {
		HorarioResponseDto r = new HorarioResponseDto();
		r.setIdHorario(h.getIdHorario());
		r.setNombre(h.getNombre());
		r.setHoraEntrada(h.getHoraEntrada());
		r.setHoraSalida(h.getHoraSalida());
		r.setToleranciaMinutos(h.getToleranciaMinutos());
		r.setEstado(h.isEstado());
		return r;
	}
}
