package com.uisrael.empleadosapi.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "areas")
public class Area {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_area")
	private Integer idArea;

	@Column(nullable = false, unique = true, length = 100)
	private String nombre;

	@Column(length = 250)
	private String descripcion;

	private boolean estado;
}
