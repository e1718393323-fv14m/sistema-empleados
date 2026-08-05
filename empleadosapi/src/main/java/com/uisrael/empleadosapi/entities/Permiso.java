package com.uisrael.empleadosapi.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "permisos")
public class Permiso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_permiso")
	private Integer idPermiso;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_empleado", nullable = false)
	private Empleado empleado;

	@Column(name = "fecha_solicitud", nullable = false)
	private LocalDateTime fechaSolicitud;

	@Column(name = "fecha_desde", nullable = false)
	private LocalDate fechaDesde;

	@Column(name = "fecha_hasta", nullable = false)
	private LocalDate fechaHasta;

	@Column(name = "tipo_permiso", nullable = false, length = 20)
	private String tipoPermiso;

	@Column(nullable = false, length = 300)
	private String motivo;

	@Column(nullable = false, length = 15)
	private String estado = "PENDIENTE";

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aprobado_por")
	private Usuario aprobadoPor;

	@Column(name = "fecha_resolucion")
	private LocalDateTime fechaResolucion;

	@Column(name = "observacion_rrhh", length = 300)
	private String observacionRrhh;
}
