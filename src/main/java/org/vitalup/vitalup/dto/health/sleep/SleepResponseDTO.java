package org.vitalup.vitalup.dto.health.sleep;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class SleepResponseDTO {

    private UUID id;
    private LocalDate date;
    private LocalDateTime sleepStart;
    private LocalDateTime wakeUp;
    private int totalMinutes;
}
