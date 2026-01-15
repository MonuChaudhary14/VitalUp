package org.vitalup.vitalup.service.Interface.Health;

import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.health.AddWaterRequestDTO;
import org.vitalup.vitalup.dto.health.WaterIntakeResponseDTO;
import org.vitalup.vitalup.entities.Auth.Users;

public interface WaterIntakeInterface {

    ApiResponse<WaterIntakeResponseDTO> addWater(Users user, AddWaterRequestDTO request);

    ApiResponse<WaterIntakeResponseDTO> getToday(Users user);

}
