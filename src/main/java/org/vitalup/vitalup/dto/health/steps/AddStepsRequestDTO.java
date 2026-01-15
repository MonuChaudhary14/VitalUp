package org.vitalup.vitalup.dto.health.steps;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddStepsRequestDTO {

    @NotNull
    @Min(0)
    private Integer steps;

    @NotNull
    @Min(0)
    private Double distanceKm;
}