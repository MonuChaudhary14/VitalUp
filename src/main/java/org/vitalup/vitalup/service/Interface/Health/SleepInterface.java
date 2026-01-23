package org.vitalup.vitalup.service.Interface.Health;

import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.health.sleep.AddSleepRequestDTO;
import org.vitalup.vitalup.dto.health.sleep.SleepResponseDTO;
import org.vitalup.vitalup.entities.Auth.Users;

public interface SleepInterface {

    ApiResponse<SleepResponseDTO> addSleep(Users user, AddSleepRequestDTO request);
    ApiResponse<SleepResponseDTO> getToday(Users user);
}
