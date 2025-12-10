package org.vitalup.vitalup.controller.Auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.service.AuthService.AuthInterface;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class OAuthRegistrationController {

	private final AuthInterface authService;

	@PostMapping("/google")
	public ResponseEntity<ApiResponse<String>> registerWithGoogle(
		@RequestBody Map<String, String> payload) {
		String email = payload.get("email");
		String username = payload.get("username");
		String googleId = payload.get("googleId");

		ApiResponse<String> response = authService.registerOrLoginWithGoogle(email, username, googleId);
		return ResponseEntity.status(response.getStatus()).body(response);
	}
}
