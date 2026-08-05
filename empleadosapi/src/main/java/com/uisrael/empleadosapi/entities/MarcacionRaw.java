package com.uisrael.empleadosapi.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "marcaciones_raw")
public class MarcacionRaw {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_marcacion")
	private Long idMarcacion;

	@Column(name = "codigo_alterno", nullable = false, length = 20)
	private String codigoAlterno;

	@Column(nullable = false)
	private LocalDate fecha;

	@Column(nullable = false)
	private LocalTime hora;

	@Column(name = "estado_marcacion", length = 20)
	private String estadoMarcacion;

	@Column(nullable = false)
	private boolean procesado = false;
}
