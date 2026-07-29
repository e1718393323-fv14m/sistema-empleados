package com.uisrael.empleadosweb.model.dto.response;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EmpleadoResponseDto {
	private Integer idEmpleado;
	private String codigoAlterno;
	private String nombres;
	private String apellidos;
	private String cedula;
	private String email;
	private String telefono;
	private LocalDate fechaNacimiento;
	private String estadoCivil;
	private String sexo;
	private String nivelEstudios;
	private String ciudad;
	private String direccion;
	private String puesto;
	private LocalDate fechaIngreso;
	private boolean estado;
	private Integer idDepartamento;
	private String nombreDepartamento;
	private String nombreArea;
	private Integer idHorario;
	private String nombreHorario;
}
