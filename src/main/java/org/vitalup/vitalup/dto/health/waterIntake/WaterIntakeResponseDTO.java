package org.vitalup.vitalup.dto.health.waterIntake;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class WaterIntakeResponseDTO {

    private UUID id;
    private LocalDate date;
    private int consumedMl;
    private int dailyGoalMl;
    private int progressPercent;

}
