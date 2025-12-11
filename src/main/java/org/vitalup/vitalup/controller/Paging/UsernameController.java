package org.vitalup.vitalup.controller.Paging;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vitalup.vitalup.service.Interface.IUsernameService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UsernameController {

	private final IUsernameService usernameService;

	@GetMapping("/check-username")
	public ResponseEntity<?> checkUsername(@RequestParam String username) {

		if (username == null || username.trim().isEmpty()) {
			return ResponseEntity.badRequest().body(
				new UsernameAvailability(false, "Username cannot be empty")
			);
		}

		boolean exists = usernameService.usernameExists(username);

		if (exists) {
			return ResponseEntity.ok(new UsernameAvailability(false,
				"Username already taken"));
		} else {
			return ResponseEntity.ok(new UsernameAvailability(true,
				"Username is available"));
		}
	}

	public record UsernameAvailability(boolean available, String message) {}
}
