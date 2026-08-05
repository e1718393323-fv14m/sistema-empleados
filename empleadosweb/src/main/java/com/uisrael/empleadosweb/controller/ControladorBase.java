package com.uisrael.empleadosweb.controller;

import java.util.function.Supplier;

import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public abstract class ControladorBase {

	protected String ejecutar(RedirectAttributes flash, String mensajeExito,
			String redireccion, Supplier<Object> accion) {
		try {
			accion.get();
			flash.addFlashAttribute("mensaje", mensajeExito);
		} catch (WebClientResponseException ex) {
			flash.addFlashAttribute("error", mensajeError(ex));
		}
		return redireccion;
	}

	protected String mensajeError(WebClientResponseException ex) {
		String cuerpo = ex.getResponseBodyAsString();
		if (cuerpo != null && cuerpo.contains("\"error\"")) {
			int inicio = cuerpo.indexOf(":\"") + 2;
			int fin = cuerpo.lastIndexOf("\"");
			if (inicio > 1 && fin > inicio) {
				return cuerpo.substring(inicio, fin);
			}
		}
		return "Ocurrio un error al comunicarse con el API";
	}
}
