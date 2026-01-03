package org.vitalup.vitalup.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vitalup.vitalup.dto.Auth.Login.LoginResponseDTO;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.repository.Auth.userRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationService {

	private final UserNameService userNameService;
	private final PasswordEncoder passwordEncoder;
	private final userRepository userRepo;

	public boolean checkCredentials(Users user, String password) {
		if (user == null || password == null) return false;
		return passwordEncoder.matches(password, user.getPassword());
	}

	public LoginResponseDTO generateTokens(Users user) {
		String accessToken = userNameService.generateToken(user);

		String refreshToken = UUID.randomUUID().toString();
		user.setRefreshToken(refreshToken);
		user.setRefreshTokenExpiry(LocalDateTime.now().plusDays(7));
		userRepo.save(user);

		return new LoginResponseDTO(accessToken, refreshToken);
	}
}
