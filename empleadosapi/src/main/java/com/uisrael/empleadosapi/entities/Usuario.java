package com.uisrael.empleadosapi.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Integer idUsuario;

	@Column(nullable = false, unique = true, length = 50)
	private String username;

	@Column(nullable = false, length = 100)
	private String password;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empleado")
	private Empleado empleado;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_rol", nullable = false)
	private Rol rol;

	@Column(nullable = false)
	private boolean estado = true;

	@Column(name = "ultimo_ingreso")
	private LocalDateTime ultimoIngreso;
}
