package org.vitalup.vitalup.dto.health.sleep;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddSleepRequestDTO {

    @NotNull
    private LocalDateTime sleepStart;

    @NotNull
    private LocalDateTime wakeUp;
}