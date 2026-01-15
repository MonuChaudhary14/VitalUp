package org.vitalup.vitalup.service.Interface.Health;

import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.health.steps.AddStepsRequestDTO;
import org.vitalup.vitalup.entities.Auth.Users;

public interface StepsInterface {

    ApiResponse<org.vitalup.vitalup.features.steps.dto.StepsResponseDTO> addSteps(Users user, AddStepsRequestDTO request);
    ApiResponse<org.vitalup.vitalup.features.steps.dto.StepsResponseDTO> getToday(Users user);
}
