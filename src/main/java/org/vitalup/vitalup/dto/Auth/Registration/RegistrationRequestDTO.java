package org.vitalup.vitalup.dto.Auth.Registration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequestDTO {

    @NotBlank(message = "email is missing")
    @Size(min = 1, max = 100, message = "Email should be under 100 characters")
    @Pattern(
            regexp = "^\\s*[^\\s@]+@[^\\s@]+\\.[^\\s@]+\\s*$",
            message = "Invalid email format or contains spaces inside"
    )
    String email;

    @NotBlank(message = "Username is missing")
    // Remember -> Add more validations
    String username;

    @NotBlank(message = "Password is missing")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?!.*\\s).+$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, " +
                    "one special character, and no spaces"
    )
    @Size(max = 16, message = "Password must not exceed 16 characters")
    String password;

}
