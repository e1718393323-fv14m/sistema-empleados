package com.uisrael.empleadosapi.services.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uisrael.empleadosapi.entities.Asistencia;
import com.uisrael.empleadosapi.entities.Empleado;
import com.uisrael.empleadosapi.entities.Horario;
import com.uisrael.empleadosapi.entities.MarcacionRaw;
import com.uisrael.empleadosapi.model.dto.request.MarcacionRawRequestDto;
import com.uisrael.empleadosapi.model.dto.response.ProcesoMarcacionResponseDto;
import com.uisrael.empleadosapi.repository.IAsistenciaRepository;
import com.uisrael.empleadosapi.repository.IEmpleadoRepository;
import com.uisrael.empleadosapi.repository.IMarcacionRawRepository;
import com.uisrael.empleadosapi.services.IMarcacionService;

/**
 * Consolidacion de marcaciones del biometrico hacia asistencias.
 *
 * Regla de negocio: por cada codigo_alterno y fecha se toman TODAS las
 * marcaciones del dia y se resuelve:
 *   - hora de entrada = MIN(hora)
 *   - hora de salida  = MAX(hora)  (solo si hay mas de una marcacion)
 *   - estado PUNTUAL/ATRASO segun el horario del empleado + tolerancia
 *
 * El proceso es idempotente (las filas quedan marcadas como procesadas) y
 * NO destruye marcaciones manuales existentes: si ya hay asistencia para el
 * dia, solo amplia el rango (entrada mas temprana / salida mas tardia).
 */
@Service
public class MarcacionServiceImpl implements IMarcacionService {

	private final IMarcacionRawRepository marcacionRepository;
	private final IEmpleadoRepository empleadoRepository;
	private final IAsistenciaRepository asistenciaRepository;

	public MarcacionServiceImpl(IMarcacionRawRepository marcacionRepository,
			IEmpleadoRepository empleadoRepository,
			IAsistenciaRepository asistenciaRepository) {
		this.marcacionRepository = marcacionRepository;
		this.empleadoRepository = empleadoRepository;
		this.asistenciaRepository = asistenciaRepository;
	}

	@Override
	@Transactional
	public int cargar(List<MarcacionRawRequestDto> marcaciones) {
		List<MarcacionRaw> entidades = marcaciones.stream().map(dto -> {
			MarcacionRaw m = new MarcacionRaw();
			m.setCodigoAlterno(dto.getCodigoAlterno());
			m.setFecha(dto.getFecha());
			m.setHora(dto.getHora());
			m.setEstadoMarcacion(dto.getEstadoMarcacion());
			m.setProcesado(false);
			return m;
		}).toList();
		return marcacionRepository.saveAll(entidades).size();
	}

	@Override
	@Transactional
	public ProcesoMarcacionResponseDto procesar(LocalDate fecha) {
		List<MarcacionRaw> pendientes = (fecha != null)
				? marcacionRepository.listarPendientesPorFecha(fecha)
				: marcacionRepository.listarPendientes();

		ProcesoMarcacionResponseDto resumen = new ProcesoMarcacionResponseDto();
		resumen.setMarcacionesLeidas(pendientes.size());

		// Agrupar por codigo_alterno + fecha (clave del dia de trabajo)
		Map<String, List<MarcacionRaw>> grupos = new LinkedHashMap<>();
		for (MarcacionRaw m : pendientes) {
			String clave = m.getCodigoAlterno() + "|" + m.getFecha();
			grupos.computeIfAbsent(clave, k -> new java.util.ArrayList<>()).add(m);
		}

		for (List<MarcacionRaw> grupo : grupos.values()) {
			String codigoAlterno = grupo.get(0).getCodigoAlterno();
			LocalDate dia = grupo.get(0).getFecha();

			// Match del codigo alterno contra la ficha del empleado
			Empleado empleado = empleadoRepository
					.buscarPorCodigoAlterno(codigoAlterno).orElse(null);

			if (empleado == null) {
				// No hace match: se deja pendiente para revision
				resumen.setSinEmpleado(resumen.getSinEmpleado() + grupo.size());
				if (!resumen.getCodigosSinMatch().contains(codigoAlterno)) {
					resumen.getCodigosSinMatch().add(codigoAlterno);
				}
				continue;
			}

			// hora minima = entrada, hora maxima = salida
			LocalTime horaMin = grupo.stream().map(MarcacionRaw::getHora)
					.min(Comparator.naturalOrder()).orElse(null);
			LocalTime horaMax = grupo.stream().map(MarcacionRaw::getHora)
					.max(Comparator.naturalOrder()).orElse(null);
			LocalTime horaSalida = (grupo.size() > 1 && !horaMax.equals(horaMin))
					? horaMax : null;

			Asistencia asistencia = asistenciaRepository
					.buscarPorEmpleadoYFecha(empleado.getIdEmpleado(), dia).orElse(null);

			if (asistencia == null) {
				asistencia = new Asistencia();
				asistencia.setEmpleado(empleado);
				asistencia.setCodigoAlterno(empleado.getCodigoAlterno());
				asistencia.setFecha(dia);
				asistencia.setHoraEntrada(horaMin);
				asistencia.setHoraSalida(horaSalida);
				asistencia.setEstadoMarcacion(calcularEstado(empleado, horaMin));
				asistencia.setObservacion("Marcacion automatica (biometrico)");
				resumen.setAsistenciasCreadas(resumen.getAsistenciasCreadas() + 1);
			} else {
				// Ya existe (posible marcacion manual): solo ampliar el rango
				boolean cambio = false;
				if (asistencia.getHoraEntrada() == null
						|| horaMin.isBefore(asistencia.getHoraEntrada())) {
					asistencia.setHoraEntrada(horaMin);
					asistencia.setEstadoMarcacion(calcularEstado(empleado, horaMin));
					cambio = true;
				}
				LocalTime salidaCandidata = (horaSalida != null) ? horaSalida : horaMax;
				if (salidaCandidata != null
						&& !salidaCandidata.equals(asistencia.getHoraEntrada())
						&& (asistencia.getHoraSalida() == null
							|| salidaCandidata.isAfter(asistencia.getHoraSalida()))) {
					asistencia.setHoraSalida(salidaCandidata);
					cambio = true;
				}
				if (asistencia.getCodigoAlterno() == null) {
					asistencia.setCodigoAlterno(empleado.getCodigoAlterno());
					cambio = true;
				}
				if (cambio) {
					resumen.setAsistenciasActualizadas(resumen.getAsistenciasActualizadas() + 1);
				}
			}
			asistenciaRepository.save(asistencia);

			// Marcar el grupo como procesado (idempotencia)
			grupo.forEach(m -> m.setProcesado(true));
			marcacionRepository.saveAll(grupo);
		}
		return resumen;
	}

	// Misma regla que la marcacion manual: horario + tolerancia
	private String calcularEstado(Empleado empleado, LocalTime horaEntrada) {
		Horario horario = empleado.getHorario();
		int tolerancia = horario.getToleranciaMinutos() != null
				? horario.getToleranciaMinutos() : 0;
		LocalTime limite = horario.getHoraEntrada().plusMinutes(tolerancia);
		return horaEntrada.isAfter(limite) ? "ATRASO" : "PUNTUAL";
	}
}
