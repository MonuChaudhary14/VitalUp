package org.vitalup.vitalup.service.AuthService;

import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.Auth.Login.LoginDTO;
import org.vitalup.vitalup.dto.Auth.Login.LoginResponseDTO;

public interface AuthInterface {

    ApiResponse<LoginResponseDTO> login(LoginDTO request);

}
