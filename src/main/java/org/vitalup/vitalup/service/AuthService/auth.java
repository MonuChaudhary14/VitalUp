package org.vitalup.vitalup.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.Auth.Login.LoginDTO;
import org.vitalup.vitalup.dto.Auth.Login.LoginResponseDTO;
import org.vitalup.vitalup.dto.Auth.Registration.RegistrationRequestDTO;
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
        String email = validator.normaliseEmail(rawEmail);

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

//    public ApiResponse<String> registration(RegistrationRequestDTO request){
//
//        if(request == null || request.getEmail() == null || request.getPassword() == null || request.getUsername() == null){
//            return new ApiResponse<>(400, "Some fields are missing", null);
//        }
//
//        try{
//            String email = validator.normaliseEmail(request.getEmail());
//
//            if (!validator.checkEmail(email)){
//                throw new IllegalArgumentException("Invalid email format");
//            }
//
//            if (existsByEmail(email)){
//                throw new IllegalArgumentException("User with this email already exists");
//            }
//        }
//        catch(IllegalArgumentException e){
//            return new ApiResponse<>(400, e.getMessage(), null);
//        }
//
//
//
//    }

    private Users getUserByEmail(String email){
        return userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User does not found"));
    }

    private boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
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
