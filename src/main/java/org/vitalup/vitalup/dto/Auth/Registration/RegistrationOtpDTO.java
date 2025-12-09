package org.vitalup.vitalup.dto.Auth.Registration;

import lombok.Data;
import org.vitalup.vitalup.entities.OTP.OtpType;

@Data
public class RegistrationOtpDTO {

    private String email;
    private String otp;
    private OtpType type;
    private String Token;

}
