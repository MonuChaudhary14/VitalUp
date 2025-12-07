package org.vitalup.vitalup.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vitalup.vitalup.dto.Auth.Login.LoginResponseDTO;
import org.vitalup.vitalup.entities.Auth.Users;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final UserNameService userNameService;
    private final PasswordEncoder passwordEncoder;

    public boolean checkCredentials(Users user, String password){
        return passwordEncoder.matches(password, user.getPassword());
    }

    public LoginResponseDTO generateTokens(Users user){

        String token = userNameService.generateToken(user);

        return new LoginResponseDTO(token);

    }

}
