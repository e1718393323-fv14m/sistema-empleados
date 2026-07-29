package com.uisrael.empleadosweb.model.dto.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class EmpleadoRequestDto {
	private Integer idEmpleado;
	private String codigoAlterno;
	private String nombres;
	private String apellidos;
	private String cedula;
	private String email;
	private String telefono;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate fechaNacimiento;
	private String estadoCivil;
	private String sexo;
	private String nivelEstudios;
	private String ciudad;
	private String direccion;
	private String puesto;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate fechaIngreso;
	private boolean estado;
	private Integer idDepartamento;
	private Integer idHorario;
}
