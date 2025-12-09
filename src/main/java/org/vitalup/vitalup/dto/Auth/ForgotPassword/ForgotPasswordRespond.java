package org.vitalup.vitalup.dto.Auth.ForgotPassword;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class ForgotPasswordRespond{

    public ForgotPasswordRespond(String token){
        this.token = token;
    }

    private String token;

}
