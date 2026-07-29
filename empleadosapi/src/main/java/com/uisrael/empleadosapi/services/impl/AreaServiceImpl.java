package com.uisrael.empleadosapi.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uisrael.empleadosapi.entities.Area;
import com.uisrael.empleadosapi.exception.RecursoNoEncontradoException;
import com.uisrael.empleadosapi.model.dto.request.AreaRequestDto;
import com.uisrael.empleadosapi.model.dto.response.AreaResponseDto;
import com.uisrael.empleadosapi.repository.IAreaRepository;
import com.uisrael.empleadosapi.services.IAreaService;

@Service
public class AreaServiceImpl implements IAreaService {

	private final IAreaRepository areaRepository;

	public AreaServiceImpl(IAreaRepository areaRepository) {
		this.areaRepository = areaRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<AreaResponseDto> listar() {
		return areaRepository.findAll().stream().map(this::aDto).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public AreaResponseDto buscarPorId(Integer id) {
		return aDto(obtener(id));
	}

	@Override
	@Transactional
	public AreaResponseDto guardar(AreaRequestDto dto) {
		Area area = new Area();
		copiar(dto, area);
		return aDto(areaRepository.save(area));
	}

	@Override
	@Transactional
	public AreaResponseDto actualizar(Integer id, AreaRequestDto dto) {
		Area area = obtener(id);
		copiar(dto, area);
		return aDto(areaRepository.save(area));
	}

	@Override
	@Transactional
	public void eliminar(Integer id) {
		areaRepository.delete(obtener(id));
	}

	private Area obtener(Integer id) {
		return areaRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Area no encontrada: " + id));
	}

	private void copiar(AreaRequestDto dto, Area area) {
		area.setNombre(dto.getNombre());
		area.setDescripcion(dto.getDescripcion());
		area.setEstado(dto.isEstado());
	}

	private AreaResponseDto aDto(Area area) {
		AreaResponseDto r = new AreaResponseDto();
		r.setIdArea(area.getIdArea());
		r.setNombre(area.getNombre());
		r.setDescripcion(area.getDescripcion());
		r.setEstado(area.isEstado());
		return r;
	}
}
