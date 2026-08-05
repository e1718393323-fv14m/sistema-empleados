package com.uisrael.empleadosweb.seguridad;

import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.uisrael.empleadosweb.model.dto.request.LoginRequestDto;
import com.uisrael.empleadosweb.model.dto.response.UsuarioResponseDto;
import com.uisrael.empleadosweb.services.IUsuarioService;

/**
 * Autentica contra el endpoint /api/usuarios/login del API.
 * El API valida BCrypt y registra la auditoria en la tabla ingresos (con IP).
 */
@Component
public class ProveedorAutenticacionApi implements AuthenticationProvider {

	private final IUsuarioService usuarioService;

	public ProveedorAutenticacionApi(IUsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		LoginRequestDto dto = new LoginRequestDto();
		dto.setUsername(authentication.getName());
		dto.setPassword(String.valueOf(authentication.getCredentials()));
		if (authentication.getDetails() instanceof WebAuthenticationDetails detalles) {
			dto.setIp(detalles.getRemoteAddress());
		}
		try {
			UsuarioResponseDto u = usuarioService.login(dto);
			UsuarioSesion principal = new UsuarioSesion(u.getIdUsuario(), u.getUsername(),
					u.getRol(), u.getIdEmpleado(), u.getNombreEmpleado());
			return new UsernamePasswordAuthenticationToken(principal, null,
					List.of(new SimpleGrantedAuthority("ROLE_" + u.getRol())));
		} catch (WebClientResponseException e) {
			// El API responde 400 {"error": "Usuario o contrasena incorrectos"}
			throw new BadCredentialsException("Usuario o contrasena incorrectos");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
