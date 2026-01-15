package org.vitalup.vitalup.features.steps.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class StepsResponseDTO {

    private UUID id;
    private LocalDate date;
    private int steps;
    private double distanceKm;
    private String activityLevel;
}