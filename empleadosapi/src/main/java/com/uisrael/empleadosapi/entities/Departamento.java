package com.uisrael.empleadosapi.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "departamentos")
public class Departamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_departamento")
	private Integer idDepartamento;

	@Column(nullable = false, unique = true, length = 100)
	private String nombre;

	@Column(length = 250)
	private String descripcion;

	private boolean estado;

	// Relacion JPA: muchos departamentos pertenecen a un area
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_area", nullable = false)
	private Area area;
}
