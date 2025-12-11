package org.vitalup.vitalup.dto.Auth.Login;

public record LoginResponseDTO(
	String accessToken,
	String refreshToken
) { }
