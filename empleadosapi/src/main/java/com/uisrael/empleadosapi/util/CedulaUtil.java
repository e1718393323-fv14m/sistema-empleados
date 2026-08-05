package com.uisrael.empleadosapi.util;

/**
 * Validador de cedulas ecuatorianas (algoritmo modulo 10).
 *
 * Reglas oficiales del Registro Civil del Ecuador:
 *  - La cedula tiene exactamente 10 digitos numericos.
 *  - Los dos primeros digitos corresponden al codigo de provincia:
 *    01 a 24 (provincias) o 30 (ecuatorianos registrados en el exterior).
 *  - El tercer digito debe ser menor a 6 (6 = RUC de sociedades publicas,
 *    9 = RUC de sociedades privadas; no son cedulas de personas naturales).
 *  - El decimo digito es el verificador, calculado con modulo 10:
 *    los 9 primeros digitos se multiplican por los coeficientes
 *    2,1,2,1,2,1,2,1,2; si un producto es mayor a 9 se le resta 9;
 *    la suma se redondea a la decena superior y la diferencia es el verificador.
 */
public final class CedulaUtil {

	private CedulaUtil() {
		// clase utilitaria: no instanciable
	}

	public static boolean esValida(String cedula) {
		// 1) Debe tener exactamente 10 digitos numericos
		if (cedula == null || !cedula.matches("\\d{10}")) {
			return false;
		}

		// 2) Codigo de provincia valido: 01-24 o 30 (exterior)
		int provincia = Integer.parseInt(cedula.substring(0, 2));
		if (!((provincia >= 1 && provincia <= 24) || provincia == 30)) {
			return false;
		}

		// 3) Tercer digito menor a 6 (personas naturales)
		int tercerDigito = cedula.charAt(2) - '0';
		if (tercerDigito >= 6) {
			return false;
		}

		// 4) Digito verificador (modulo 10 con coeficientes 2,1,2,1,...)
		int suma = 0;
		for (int i = 0; i < 9; i++) {
			int digito = cedula.charAt(i) - '0';
			if (i % 2 == 0) { // posiciones impares (indice par): coeficiente 2
				digito *= 2;
				if (digito > 9) {
					digito -= 9;
				}
			}
			suma += digito;
		}
		int verificadorCalculado = (10 - (suma % 10)) % 10;
		int verificadorReal = cedula.charAt(9) - '0';

		return verificadorCalculado == verificadorReal;
	}
}
