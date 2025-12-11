package org.vitalup.vitalup.service.Interface;

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

public interface AuthInterface {

    ApiResponse<LoginResponseDTO> login(LoginDTO request);

    ApiResponse<String> registration(RegistrationRequestDTO request);

    ApiResponse<?> validateRegistrationOtp(RegistrationOtpDTO request);

    ApiResponse<String> forgotPassword(String email);

    ApiResponse<ForgotPasswordRespond> validateForgotOtp(ValidateForgotOtpRequest request);

    ApiResponse<String> resendOTP(ResendOtpDTO request);

    ApiResponse<String> resendForgotPasswordOtp(ResendForgotOtpRequest request);

    ApiResponse<String> resetPassword(ResetPasswordRequest request);

    ApiResponse<String> registerOrLoginWithGoogle(String email, String username, String googleId);

}
