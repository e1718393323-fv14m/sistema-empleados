package com.uisrael.empleadosapi.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

/** Auditoria de accesos al sistema (cada intento de login) */
@Data
@Entity
@Table(name = "ingresos")
public class Ingreso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_ingreso")
	private Long idIngreso;

	// Puede ser null si el username no existe
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@Column(name = "username_intentado", length = 50)
	private String usernameIntentado;

	@Column(name = "fecha_hora", nullable = false)
	private LocalDateTime fechaHora;

	@Column(length = 60)
	private String ip;

	@Column(nullable = false)
	private boolean exitoso;
}
