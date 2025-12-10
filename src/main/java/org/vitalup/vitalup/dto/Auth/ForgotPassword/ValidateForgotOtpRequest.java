package org.vitalup.vitalup.dto.Auth.ForgotPassword;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateForgotOtpRequest {

	String email;
	String otp;

}
