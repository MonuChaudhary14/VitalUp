package org.vitalup.vitalup.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.Auth.ForgotPassword.ForgotPasswordRespond;
import org.vitalup.vitalup.dto.Auth.ForgotPassword.ValidateForgotOtpRequest;
import org.vitalup.vitalup.dto.Auth.Login.LoginDTO;
import org.vitalup.vitalup.dto.Auth.Login.LoginResponseDTO;
import org.vitalup.vitalup.dto.Auth.Registration.RegistrationOtpDTO;
import org.vitalup.vitalup.dto.Auth.Registration.RegistrationRequestDTO;
import org.vitalup.vitalup.dto.Auth.ResendOtp.ResendForgotOtpRequest;
import org.vitalup.vitalup.dto.Auth.ResendOtp.ResendOtpDTO;
import org.vitalup.vitalup.dto.Auth.ResetPassword.ResetPasswordRequest;
import org.vitalup.vitalup.entities.Auth.UserRole;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.entities.OTP.OtpType;
import org.vitalup.vitalup.repository.Auth.userRepository;
import org.vitalup.vitalup.security.EmailValidator;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.vitalup.vitalup.security.UsernameValidator;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class auth implements AuthInterface {

	private final EmailValidator validator;
	private final UsernameValidator usernameValidator;
	private final userRepository userRepo;
	private final VerificationService verificationService;
	private final OTPService otpService;
	private final PasswordEncoder passwordEncoder;
	private final UserNameService usernameService;
	private final RedisService redisService;
	private static final long FORGOT_TOKEN_EXPIRE_SECONDS = 295929000L;
	private static final int TEMP_TOKEN_EXPIRE = 300;

	public ApiResponse<LoginResponseDTO> login(LoginDTO request) {

		if (request == null || request.getUsername() == null || request.getPassword() == null) {
			return new ApiResponse<>(400, "PLease check all the fields", null);
		}

		String rawUsername = request.getUsername();
		String rawPassword = request.getPassword();

		Users user;

		try {
			if (usernameValidator.checkUsername(rawUsername)) {
				user = getUserByUsername(rawUsername);
			} else if (validator.checkEmail(rawUsername)) {
				user = getUserByEmail(rawUsername);
			} else {
				return new ApiResponse<>(400,
					"Invalid username or email format", null);
			}

		} catch (UsernameNotFoundException e) {
			return new ApiResponse<>(400, "User not found", null);
		}

		if (!Boolean.TRUE.equals(user.getEnabled())) {
			return new ApiResponse<>(403,
				"Account not enabled. Please verify your OTP first.", null);
		}

		if (!verificationService.checkCredentials(user, request.getPassword())) {
			return new ApiResponse<>(401, "Invalid credentials", null);
		}

//        if (isRefreshTokenExpired(user)){
//
//            LoginResponseDTO newToken = generateTokensOrNull(user);
//
//            if (newToken == null){
//                return new ApiResponse<>(500, "Failed to generate tokens", null);
//            }
//
//            user.setRefreshToken(newToken.getRefreshToken());
//            user.setRefreshTokenExpiry(LocalDateTime.now().plusDays(7));  // 7 days, example
//            userRepository.save(user);
//
//            return new ApiResponse<>(200, "Logged in successfully (new refresh token issued)", newToken);
//        }

		LoginResponseDTO token = generateTokensOrNull(user);
		if (token == null) {
			return new ApiResponse<>(500, "Failed to generate tokens", null);
		}

		return new ApiResponse<>(200, "Logged in successfully", token);
	}

	public ApiResponse<String> registration(RegistrationRequestDTO request) {

		if (request == null || request.getEmail() == null || request.getPassword() == null ||
			request.getUsername() == null) {
			return new ApiResponse<>(400, "Some fields are missing", null);
		}
		String email;
		try {
			email = validator.normaliseEmail(request.getEmail());

			if (!validator.checkEmail(email)) {
				throw new IllegalArgumentException("Invalid email format");
			}

			if (existsByEmail(email)) {
				throw new IllegalArgumentException("User with this email already exists");
			}
		} catch (IllegalArgumentException e) {
			return new ApiResponse<>(400, e.getMessage(), null);
		}

		if (otpService.isInCooldown(email, OtpType.REGISTRATION)) {
			long secondsLeft = otpService.cooldownTime(email, OtpType.REGISTRATION);
			return new ApiResponse<>(400, "Wait for " + secondsLeft, null);
		}

		RegistrationRequestDTO temp = new RegistrationRequestDTO();

		temp.setEmail(email);
		temp.setPassword(passwordEncoder.encode(request.getPassword()));
		temp.setUsername(request.getUsername());

		try {
			otpService.saveTempOtp(email, temp);
			otpService.sendOTP(email, OtpType.REGISTRATION);

			String token = usernameService.generateRegistrationToken(email);
			redisService.saveValue("REGISTRATION_SESSION_" + email, token, TEMP_TOKEN_EXPIRE);
			return new ApiResponse<>(200, "OTP sent. Please check your mail", token);
		} catch (RuntimeException e) {
			return new ApiResponse<>(500, "Registration failed", null);
		}

	}

	public ApiResponse<?> validateRegistrationOtp(RegistrationOtpDTO request) {

		if (request == null || request.getEmail() == null || request.getOtp() == null ||
			request.getType() == null || request.getToken() == null) {
			return new ApiResponse<>(400, "Check your fields");
		}

		String rawEmail = request.getEmail();
		String email;

		try {
			if (validator.checkEmail(rawEmail)) {
				email = validator.normaliseEmail(rawEmail);
			} else {
				throw new IllegalArgumentException("Invalid email format.");
			}
		} catch (IllegalArgumentException e) {
			return new ApiResponse<>(400, e.getMessage(), null);
		}

		OtpType type = request.getType();

		if (type != OtpType.REGISTRATION) {
			return new ApiResponse<>(400, "Only registration OTP can be validated here.",
				null);
		}

		if (otpService.isBlocked(email, type)) {
			return new ApiResponse<>(429,
				"Too many invalid OTP attempts. Try again later.", null);
		}

		if (otpService.isUsed(email, type)) {
			return new ApiResponse<>(400, "OTP has already been used", null);
		}

		boolean valid = otpService.validateOTP(email, request.getOtp(), type);

		if (!valid) {
			long attempts = otpService.incrementOtpAttempts(email, type);
			if (attempts >= 3) {
				otpService.blockOtp(email, type);
				return new ApiResponse<>(429,
					"Too many invalid attempts. Please request a new OTP.", null);
			}
			return new ApiResponse<>(400, "Invalid or expired OTP. Attempts left: " +
				(3 - attempts), null);
		}
		otpService.markAsUsed(email, request.getOtp(), type);

		try {
			RegistrationRequestDTO tempRequest = otpService.getTempOtp(email);
			if (tempRequest == null) {
				return new ApiResponse<>(400, "Registration session expired or not found",
					null);
			}

			if (isUserAlreadyRegistered(tempRequest)) {
				return new ApiResponse<>(400, "User already registered", null);
			}

			Users newUser = buildUserFromRequest(tempRequest);
			userRepo.save(newUser);

			otpService.deleteTempOtp(email);
			otpService.deleteOTP(email, OtpType.REGISTRATION);
			otpService.deleteRegistrationSessionToken(email);

			return new ApiResponse<>(200,
				"Registration verified successfully", null);

		} catch (Exception e) {
			return new ApiResponse<>(500, "Registration verification failed: " +
				e.getMessage(), null);
		}
	}

	public ApiResponse<String> forgotPassword(String email) {
		if (email == null || email.isBlank()) {
			return new ApiResponse<>(400, "Check your fields", null);
		}

		String normalizedEmail = validator.normaliseEmail(email);
		boolean userExists = userRepo.existsByEmail(normalizedEmail);
		String sessionToken = UUID.randomUUID().toString();

		try {
			if (userExists) {
				otpService.sendOTP(normalizedEmail, OtpType.FORGOT_PASSWORD);
				redisService.saveValue("FORGOT_SESSION_" + normalizedEmail, sessionToken,
					TEMP_TOKEN_EXPIRE);
			}
			String message = "If this email exists, an OTP has been sent to your inbox";
			String data = null;
			if (userExists) {
				data = sessionToken;
			}

			return new ApiResponse<>(200, message, data);

		} catch (RuntimeException e) {
			return new ApiResponse<>(500, "Failed to send OTP", null);
		}
	}

	public ApiResponse<ForgotPasswordRespond> validateForgotOtp(ValidateForgotOtpRequest request) {
		if (request.getEmail() == null || request.getOtp() == null || request.getToken() == null) {
			return new ApiResponse<>(400, "Check your fields", null);
		}

		String rawEmail = request.getEmail();
		String email;
		String otp = request.getOtp();
		String sessionToken = request.getToken();

		try {
			if (validator.checkEmail(rawEmail)) {
				email = validator.normaliseEmail(rawEmail);
			} else {
				throw new IllegalArgumentException();
			}
		} catch (IllegalArgumentException e) {
			return new ApiResponse<>(400, "Check the email format", null);
		}

		String savedSession = redisService.getValue("FORGOT_SESSION_" + email);
		if (savedSession == null || !savedSession.equals(sessionToken)) {
			return new ApiResponse<>(403, "Invalid or expired session token", null);
		}

		if (otpService.isBlocked(email, OtpType.FORGOT_PASSWORD)) {
			return new ApiResponse<>(429,
				"Too many invalid OTP attempts. Try again later.", null);
		}

		if (otpService.isUsed(email, OtpType.FORGOT_PASSWORD)) {
			return new ApiResponse<>(400, "OTP has already been used", null);
		}

		boolean valid = otpService.validateOTP(email, otp, OtpType.FORGOT_PASSWORD);

		if (!valid) {
			long attempts = otpService.incrementOtpAttempts(email, OtpType.FORGOT_PASSWORD);
			if (attempts >= 3) {
				otpService.blockOtp(email, OtpType.FORGOT_PASSWORD);
				return new ApiResponse<>(429,
					"Too many invalid attempts. Request a new OTP.", null);
			}
			return new ApiResponse<>(400,
				"Invalid or expired OTP. Attempts left: " + (3 - attempts), null);
		}

		otpService.markAsUsed(email, otp, OtpType.FORGOT_PASSWORD);

		String resetToken = UUID.randomUUID().toString();

		int resetTtlSeconds = 15 * 60;
		redisService.saveValue("TEMP_RESET_" + resetToken, email, resetTtlSeconds);

		redisService.deleteValue("FORGOT_SESSION_" + email);

		ForgotPasswordRespond tokenResponse = new ForgotPasswordRespond(resetToken);
		return new ApiResponse<>(200, "OTP validated successfully", tokenResponse);
	}

	public ApiResponse<String> resendOTP(ResendOtpDTO request) {

		if (request.getEmail() == null || request.getToken() == null) {
			return new ApiResponse<>(400, "Check all the fields");
		}

		String rawEmail = request.getEmail();
		String token = request.getToken();
		String email;

		if (validator.checkEmail(rawEmail)) {
			email = validator.normaliseEmail(rawEmail);
		} else {
			return new ApiResponse<>(400, "Check the email format", null);
		}

		String savedToken = redisService.getValue("REGISTRATION_SESSION_" + email);
		boolean sessionValid = savedToken != null && savedToken.equals(token);

		if (!sessionValid) {
			return new ApiResponse<>(403,
				"Invalid or expired registration session token", null);
		}

		if (isUserAlreadyVerified(email)) {
			return new ApiResponse<>(400, "User already verified. No OTP needed.",
				null);
		}

		if (otpService.isInCooldown(email, OtpType.REGISTRATION)) {
			long secondsLeft = otpService.cooldownTime(email, OtpType.REGISTRATION);
			return new ApiResponse<>(400, "Please wait " + secondsLeft +
				" seconds before requesting OTP again.", null);
		}

		try {
			otpService.sendOTP(email, OtpType.REGISTRATION);
			return new ApiResponse<>(200, "OTP resent successfully.", null);
		} catch (RuntimeException e) {
			return new ApiResponse<>(429, e.getMessage(), null);
		}

	}

	public ApiResponse<String> resendForgotPasswordOtp(ResendForgotOtpRequest request) {
		if (request.getToken() == null) {
			return new ApiResponse<>(400, "Check your fields", null);
		}

		String token = request.getToken();

		String email = otpService.extractEmailFromToken(token, OtpType.FORGOT_PASSWORD);

		if (email == null) {
			return new ApiResponse<>(403, "Session expired or invalid", null);
		}

		if (otpService.isInCooldown(email, OtpType.FORGOT_PASSWORD)) {
			long secondsLeft = otpService.cooldownTime(email, OtpType.FORGOT_PASSWORD);
			return new ApiResponse<>(400, "Please wait " + secondsLeft +
				" seconds before requesting OTP again.", null);
		}

		Users user;
		try {
			if (validator.checkEmail(email)) {
				user = getUserByEmail(email);
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			return new ApiResponse<>(404, "User not found", null);
		}

		try {
			String newTempToken = sendForgotPasswordOtpAndCreateTempToken(email, user);
			return new ApiResponse<>(200, "OTP resent successfully.", newTempToken);
		} catch (RuntimeException e) {
			return new ApiResponse<>(429, e.getMessage(), null);
		}

	}

	public ApiResponse<String> resetPassword(ResetPasswordRequest request) {

		if (request.getToken() == null || request.getNewPassword() == null) {
			return new ApiResponse<>(400, "Check your fields", null);
		}

		String tempToken = request.getToken();
		String newPassword = request.getNewPassword();

		String email = redisService.getValue("TEMP_RESET_" + tempToken);
		if (email == null) {
			return new ApiResponse<>(401, "Invalid or expired reset token.", null);
		}

		Users user;
		try {
			user = getUserByEmail(email);
		} catch (Exception e) {
			return new ApiResponse<>(400, "User not found", null);
		}

		user.setPassword(passwordEncoder.encode(newPassword));

		int currentVersion = user.getPasswordVersion();
		user.setPasswordVersion(currentVersion + 1);

		userRepo.save(user);

		redisService.deleteValue("TEMP_RESET_" + tempToken);

		return new ApiResponse<>(200, "Password has been reset successfully.",
			null);
	}

	private Users getUserByEmail(String email) {
		return userRepo.findByEmail(email).orElseThrow(() ->
			new UsernameNotFoundException("User does not found"));
	}

	private boolean existsByEmail(String email) {
		return userRepo.existsByEmail(email);
	}

	private LoginResponseDTO generateTokensOrNull(Users user) {
		try {
			return verificationService.generateTokens(user);
		} catch (Exception e) {
			return null;
		}
	}

	private boolean isUserAlreadyRegistered(RegistrationRequestDTO tempRequest) {
		return tempRequest.getEmail() != null && existsByEmail(tempRequest.getEmail());
	}

	private Users buildUserFromRequest(RegistrationRequestDTO tempRequest) {
		Users newUser = new Users();
		newUser.setEmail(tempRequest.getEmail());
		newUser.setUsername(tempRequest.getUsername());
		newUser.setPassword(tempRequest.getPassword());
		newUser.setIsVerifiedRegistration(true);
		newUser.setEnabled(true);
		newUser.setLocked(false);
		newUser.setUserRole(UserRole.USER);

		return newUser;
	}

	private String sendForgotPasswordOtpAndCreateTempToken(String email, Users user) {
		otpService.sendOTP(email, OtpType.FORGOT_PASSWORD);

		long tokenExpire = FORGOT_TOKEN_EXPIRE_SECONDS;

		String tempTokenKey = "TEMP_TOKEN_" + OtpType.FORGOT_PASSWORD + "_" + email;
		redisService.deleteValue(tempTokenKey);
		redisService.saveValue(tempTokenKey, user.getRefreshToken(), tokenExpire);

		String tokenToIdentifierKey = OtpType.FORGOT_PASSWORD.name() + "_" + user.getRefreshToken();
		redisService.saveValue(tokenToIdentifierKey, email, tokenExpire);

		return user.getRefreshToken();
	}

	private boolean isUserAlreadyVerified(String email) {
		try {
			Users existingUser;
			if (validator.checkEmail(email)) {
				existingUser = getUserByEmail(email);
			} else {
				throw new Exception();
			}
			return existingUser != null && Boolean.TRUE.equals(existingUser.getEnabled());
		} catch (Exception ignored) {
			return false;
		}
	}

	private Users getUserByUsername(String userName) {
		return userRepo.findByUsernameIgnoreCase(userName).orElseThrow(() ->
			new UsernameNotFoundException("User does not found"));
	}

	private boolean isRefreshTokenExpired(Users user) {
		return user.getRefreshToken() == null ||
			user.getRefreshTokenExpiry() == null ||
			LocalDateTime.now().isAfter(user.getRefreshTokenExpiry());
	}

	public ApiResponse<String> registerOrLoginWithGoogle(String email, String username,
																											 String googleId) {
		if (email == null || googleId == null) {
			return new ApiResponse<>(400, "Invalid Google user data", null);
		}

		Users user = userRepo.findByEmail(email).orElse(null);

		if (user == null) {
			user = new Users();
			user.setEmail(email);
			user.setUsername(username != null ? username : email.split("@")[0]);
			user.setEnabled(true);
			user.setLocked(false);
			user.setUserRole(UserRole.USER);
			user.setGoogleId(googleId);
			userRepo.save(user);
		}

		String jwt = usernameService.generateToken(user);

		return new ApiResponse<>(200, "Logged in with Google successfully", jwt);
	}

}
