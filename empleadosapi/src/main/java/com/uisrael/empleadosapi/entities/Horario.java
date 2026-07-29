package com.uisrael.empleadosapi.entities;

import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "horarios")
public class Horario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_horario")
	private Integer idHorario;

	@Column(nullable = false, unique = true, length = 100)
	private String nombre;

	@Column(name = "hora_entrada", nullable = false)
	private LocalTime horaEntrada; // 08:30

	@Column(name = "hora_salida", nullable = false)
	private LocalTime horaSalida; // 17:00

	@Column(name = "tolerancia_minutos")
	private Integer toleranciaMinutos;

	private boolean estado;
}
