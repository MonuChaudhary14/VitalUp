package org.vitalup.vitalup.dto.pushNotification;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendNotificationRequestDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String body;

}
