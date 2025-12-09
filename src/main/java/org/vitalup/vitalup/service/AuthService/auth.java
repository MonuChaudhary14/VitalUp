package org.vitalup.vitalup.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.Auth.Login.LoginDTO;
import org.vitalup.vitalup.dto.Auth.Login.LoginResponseDTO;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.repository.Auth.userRepository;
import org.vitalup.vitalup.security.EmailValidator;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@RequiredArgsConstructor
public class auth implements AuthInterface {

    private final EmailValidator validator;
    private final userRepository userRepo;
    private final VerificationService verificationService;

    public ApiResponse<LoginResponseDTO> login(LoginDTO request){

        if(request == null || request.getEmail() == null || request.getPassword() == null){
            return new ApiResponse<>(400, "PLease check all the fields", null);
        }

        String rawEmail = request.getEmail();
        String rawPassword = request.getPassword();
        String email = normaliseEmail(rawEmail);

        Users user;

        try{
            if(!validator.checkEmail(email)){
                user = getUserByEmail(email);
            }
            else{
                return new ApiResponse<>(400, "Invalid email");
            }
        }
        catch(UsernameNotFoundException e){
            return new ApiResponse<>(400, "User not found", null);
        }

        if (!verificationService.checkCredentials(user, request.getPassword())) {
            return new ApiResponse<>(401, "Invalid credentials", null);
        }

        LoginResponseDTO token = generateTokensOrNull(user);
        if (token == null) {
            return new ApiResponse<>(500, "Failed to generate tokens", null);
        }

        return new ApiResponse<>(200, "Logged in successfully", token);
    }

    private String normaliseEmail(String email){
        if(email == null) return "PLease enter the email";
        else return email.trim().toLowerCase();
    }

    private Users getUserByEmail(String email){
        return userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User does not found"));
    }

    private LoginResponseDTO generateTokensOrNull(Users user) {
        try {
            return verificationService.generateTokens(user);
        }
        catch (Exception e) {
            return null;
        }
    }

}
