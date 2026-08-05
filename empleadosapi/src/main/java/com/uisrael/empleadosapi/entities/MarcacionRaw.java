package com.uisrael.empleadosapi.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Tabla de staging para la integracion de datos del reloj biometrico.
 * Recibe multiples marcaciones por empleado y dia (identificado por
 * codigo_alterno). Un proceso las consolida en la tabla asistencias:
 * MIN(hora) = hora de entrada, MAX(hora) = hora de salida.
 */
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

	// Estado que envia el dispositivo (opcional, puede venir vacio)
	@Column(name = "estado_marcacion", length = 20)
	private String estadoMarcacion;

	// Control del proceso de consolidacion
	@Column(nullable = false)
	private boolean procesado = false;
}
