package com.uisrael.empleadosapi.model.dto.request;

import lombok.Data;

/** Datos para aprobar o rechazar un permiso (solo RRHH/ADMIN) */
@Data
public class ResolucionPermisoRequestDto {
	private Integer idUsuario; // usuario RRHH que resuelve
	private String observacion;
}
