package org.vitalup.vitalup.dto.Auth.Token;

public record RefreshTokenResponse(
	String accessToken,
	String refreshToken
) { }
