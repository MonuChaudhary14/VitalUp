package org.vitalup.vitalup.service.AuthService;

import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.Auth.ForgotPassword.ForgotPasswordRespond;
import org.vitalup.vitalup.dto.Auth.ForgotPassword.ValidateForgotOtpRequest;
import org.vitalup.vitalup.dto.Auth.Login.LoginDTO;
import org.vitalup.vitalup.dto.Auth.Login.LoginResponseDTO;
import org.vitalup.vitalup.dto.Auth.Registration.RegistrationOtpDTO;
import org.vitalup.vitalup.dto.Auth.Registration.RegistrationRequestDTO;

public interface AuthInterface {

    ApiResponse<LoginResponseDTO> login(LoginDTO request);

    ApiResponse<String> registration(RegistrationRequestDTO request);

    ApiResponse<?> validateRegistrationOtp(RegistrationOtpDTO request);

    ApiResponse<String> forgotPassword(String email);

    ApiResponse<ForgotPasswordRespond> validateForgotOtp(ValidateForgotOtpRequest request);

}
