package org.vitalup.vitalup.controller.Auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.Auth.ForgotPassword.ForgotPasswordRespond;
import org.vitalup.vitalup.dto.Auth.ForgotPassword.ValidateForgotOtpRequest;
import org.vitalup.vitalup.dto.Auth.Login.LoginDTO;
import org.vitalup.vitalup.dto.Auth.Login.LoginResponseDTO;
import org.vitalup.vitalup.dto.Auth.Registration.RegistrationOtpDTO;
import org.vitalup.vitalup.dto.Auth.Registration.RegistrationRequestDTO;
import org.vitalup.vitalup.service.AuthService.AuthInterface;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthInterface authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody LoginDTO request){
        ApiResponse<LoginResponseDTO> response = authService.login(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/registration")
    public ResponseEntity<ApiResponse<String>> registration(@Valid @RequestBody RegistrationRequestDTO request){
        ApiResponse<String> response = authService.registration(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/validateRegistration")
    public ResponseEntity<ApiResponse<?>> validateRegistrationOTP(@RequestBody RegistrationOtpDTO request){
        ApiResponse<?> response = authService.validateRegistrationOtp(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody String email){
        ApiResponse<String> response = authService.forgotPassword(email);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("validateForgotPassword")
    public ResponseEntity<ApiResponse<ForgotPasswordRespond>> validateForgotOtp(@RequestBody ValidateForgotOtpRequest request) {
        ApiResponse<ForgotPasswordRespond> response = authService.validateForgotOtp(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


}
