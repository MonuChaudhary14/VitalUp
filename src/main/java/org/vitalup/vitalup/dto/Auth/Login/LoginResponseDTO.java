package org.vitalup.vitalup.dto.Auth.Login;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginResponseDTO {

	private final String token;

}
