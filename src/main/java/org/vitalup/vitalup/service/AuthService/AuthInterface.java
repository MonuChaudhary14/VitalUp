package org.vitalup.vitalup.service.AuthService;

import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.Auth.Login.LoginDTO;
import org.vitalup.vitalup.dto.Auth.Login.LoginResponseDTO;
import org.vitalup.vitalup.dto.Auth.Registration.RegistrationRequestDTO;

public interface AuthInterface {

    ApiResponse<LoginResponseDTO> login(LoginDTO request);

//    ApiResponse<?> registration(RegistrationRequestDTO request);

}
