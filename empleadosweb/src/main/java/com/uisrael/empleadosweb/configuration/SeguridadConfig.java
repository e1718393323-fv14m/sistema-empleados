package com.uisrael.empleadosweb.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.uisrael.empleadosweb.seguridad.ProveedorAutenticacionApi;

/**
 * Seguridad del cliente web (el API queda interno; decision documentada en README):
 * - ADMIN: acceso total (usuarios, auditoria)
 * - RRHH: aprueba/rechaza permisos, gestiona empleados y catalogos
 * - EMPLEADO: marcacion remota, sus permisos y su historial
 */
@Configuration
@EnableWebSecurity
public class SeguridadConfig {

	private final ProveedorAutenticacionApi proveedor;

	public SeguridadConfig(ProveedorAutenticacionApi proveedor) {
		this.proveedor = proveedor;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authenticationProvider(proveedor)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/assets/**", "/login").permitAll()
				.requestMatchers("/usuarios/**").hasRole("ADMIN")
				.requestMatchers("/permisos/*/aprobar", "/permisos/*/rechazar")
					.hasAnyRole("ADMIN", "RRHH")
				.requestMatchers("/empleados/**", "/areas/**", "/departamentos/**",
						"/horarios/**").hasAnyRole("ADMIN", "RRHH")
				.anyRequest().authenticated())
			.formLogin(form -> form
				.loginPage("/login")
				.defaultSuccessUrl("/", true)
				.failureUrl("/login?error")
				.permitAll())
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout")
				.permitAll())
			.exceptionHandling(ex -> ex.accessDeniedPage("/acceso-denegado"));
		return http.build();
	}
}
