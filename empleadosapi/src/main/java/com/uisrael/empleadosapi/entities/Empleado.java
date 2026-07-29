package com.uisrael.empleadosapi.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "empleados")
public class Empleado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_empleado")
	private Integer idEmpleado; // CODIGO del empleado (ficha principal)

	@Column(name = "codigo_alterno", unique = true, length = 20)
	private String codigoAlterno; // Ej: ADE002886

	@Column(nullable = false, length = 100)
	private String nombres;

	@Column(nullable = false, length = 100)
	private String apellidos;

	@Column(nullable = false, unique = true, length = 13)
	private String cedula;

	@Column(length = 150)
	private String email;

	@Column(length = 15)
	private String telefono;

	@Column(name = "fecha_nacimiento")
	private LocalDate fechaNacimiento;

	@Column(name = "estado_civil", length = 20)
	private String estadoCivil;

	@Column(length = 10)
	private String sexo;

	@Column(name = "nivel_estudios", length = 30)
	private String nivelEstudios;

	@Column(length = 60)
	private String ciudad;

	@Column(length = 250)
	private String direccion;

	@Column(length = 120)
	private String puesto;

	@Column(name = "fecha_ingreso")
	private LocalDate fechaIngreso;

	private boolean estado;

	// Relacion JPA: muchos empleados pertenecen a un departamento
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_departamento", nullable = false)
	private Departamento departamento;

	// Relacion JPA: muchos empleados tienen asignado un horario
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_horario", nullable = false)
	private Horario horario;

	// Relacion JPA inversa: un empleado tiene muchas asistencias
	@OneToMany(mappedBy = "empleado", fetch = FetchType.LAZY)
	@ToString.Exclude
	private List<Asistencia> asistencias;
}
