package com.uisrael.empleadosapi.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Rol {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_rol")
	private Integer idRol;

	@Column(nullable = false, unique = true, length = 30)
	private String nombre; 

	@Column(length = 200)
	private String descripcion;
}
