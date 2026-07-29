package com.uisrael.empleadosapi.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "asistencias",
	uniqueConstraints = @UniqueConstraint(columnNames = { "id_empleado", "fecha" }))
public class Asistencia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_asistencia")
	private Integer idAsistencia;

	@Column(nullable = false)
	private LocalDate fecha;

	@Column(name = "codigo_alterno", length = 20)
	private String codigoAlterno;

	@Column(name = "hora_entrada")
	private LocalTime horaEntrada;

	@Column(name = "hora_salida")
	private LocalTime horaSalida;

	@Column(name = "estado_marcacion", length = 20)
	private String estadoMarcacion; // PUNTUAL o ATRASO

	@Column(length = 250)
	private String observacion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empleado", nullable = false)
	private Empleado empleado;
}
