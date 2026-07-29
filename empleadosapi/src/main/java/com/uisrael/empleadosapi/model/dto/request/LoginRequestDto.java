package com.uisrael.empleadosapi.model.dto.request;

import lombok.Data;

@Data
public class LoginRequestDto {
	private String username;
	private String password;
	private String ip;
}
