package org.vitalup.vitalup.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.Auth.Google.GoogleLoginRequest;
import org.vitalup.vitalup.dto.Auth.Token.RefreshTokenResponse;
import org.vitalup.vitalup.service.Interface.AuthInterface;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class OAuthRegistrationController {

	private final AuthInterface authService;

	@PostMapping("/google")
	public ResponseEntity<ApiResponse<RefreshTokenResponse>> googleLogin(@RequestBody GoogleLoginRequest request){
		ApiResponse<RefreshTokenResponse> response = authService.registerOrLoginWithGoogle(request.idToken());
		return ResponseEntity.status(response.getStatus()).body(response);
	}
}
