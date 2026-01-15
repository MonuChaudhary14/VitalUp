package org.vitalup.vitalup.dto.pushNotification;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterDeviceRequestDTO {

    @NotBlank
    private String token;

    @NotBlank
    private String platform;
}