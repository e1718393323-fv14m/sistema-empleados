package com.uisrael.empleadosapi.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uisrael.empleadosapi.entities.Area;
import com.uisrael.empleadosapi.entities.Departamento;
import com.uisrael.empleadosapi.exception.RecursoNoEncontradoException;
import com.uisrael.empleadosapi.model.dto.request.DepartamentoRequestDto;
import com.uisrael.empleadosapi.model.dto.response.DepartamentoResponseDto;
import com.uisrael.empleadosapi.repository.IAreaRepository;
import com.uisrael.empleadosapi.repository.IDepartamentoRepository;
import com.uisrael.empleadosapi.services.IDepartamentoService;

@Service
public class DepartamentoServiceImpl implements IDepartamentoService {

	private final IDepartamentoRepository departamentoRepository;
	private final IAreaRepository areaRepository;

	public DepartamentoServiceImpl(IDepartamentoRepository departamentoRepository,
			IAreaRepository areaRepository) {
		this.departamentoRepository = departamentoRepository;
		this.areaRepository = areaRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DepartamentoResponseDto> listar() {
		// usa la consulta JPQL con JOIN FETCH
		return departamentoRepository.listarConArea().stream().map(this::aDto).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public DepartamentoResponseDto buscarPorId(Integer id) {
		return aDto(obtener(id));
	}

	@Override
	@Transactional
	public DepartamentoResponseDto guardar(DepartamentoRequestDto dto) {
		Departamento d = new Departamento();
		copiar(dto, d);
		return aDto(departamentoRepository.save(d));
	}

	@Override
	@Transactional
	public DepartamentoResponseDto actualizar(Integer id, DepartamentoRequestDto dto) {
		Departamento d = obtener(id);
		copiar(dto, d);
		return aDto(departamentoRepository.save(d));
	}

	@Override
	@Transactional
	public void eliminar(Integer id) {
		departamentoRepository.delete(obtener(id));
	}

	private Departamento obtener(Integer id) {
		return departamentoRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Departamento no encontrado: " + id));
	}

	private void copiar(DepartamentoRequestDto dto, Departamento d) {
		d.setNombre(dto.getNombre());
		d.setDescripcion(dto.getDescripcion());
		d.setEstado(dto.isEstado());
		Area area = areaRepository.findById(dto.getIdArea())
				.orElseThrow(() -> new RecursoNoEncontradoException("Area no encontrada: " + dto.getIdArea()));
		d.setArea(area);
	}

	private DepartamentoResponseDto aDto(Departamento d) {
		DepartamentoResponseDto r = new DepartamentoResponseDto();
		r.setIdDepartamento(d.getIdDepartamento());
		r.setNombre(d.getNombre());
		r.setDescripcion(d.getDescripcion());
		r.setEstado(d.isEstado());
		r.setIdArea(d.getArea().getIdArea());
		r.setNombreArea(d.getArea().getNombre());
		return r;
	}
}
